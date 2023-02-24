package dinf.auth

import dinf.db.firstOrNull
import dinf.db.getPLong
import dinf.db.setPLong
import dinf.db.sql
import dinf.domain.ID
import dinf.domain.User
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.serialization.Serializable

class OAuthService(private val httpClient: HttpClient) {

    suspend fun addLogin(user: User, credential: Credential.Google) {
        val googleUser = findInGoogle(credential.token)
        requireNotNull(googleUser) { "Bad credentials=$credential passed for Google auth" }

        sql("INSERT INTO login_oauth_google (user, email) VALUES (?, ?)") {
            setPLong(1, user.id.number)
            setString(2, googleUser.email)
            execute()
        }
    }

    suspend fun findUser(credential: Credential.Google): CredentialValidation {
        val googleUser = findInGoogle(credential.token) ?: return CredentialValidation.Invalid

        val user = sql(
            """
            SELECT users.id, users.name 
            FROM users
            JOIN login_oauth_google ON users.id = login_oauth_google.user
            WHERE login_oauth_google.email = ?
            """.trimIndent()
        ) {
            setString(1, googleUser.email)
            executeQuery().firstOrNull {
                User(
                    id = ID(getPLong("id")),
                    name = getString("name"),
                )
            }
        } ?: return CredentialValidation.NotFound

        return CredentialValidation.Ok(UserPrincipal(user))
    }

    private suspend fun findInGoogle(token: String): GoogleUser? {
        val response = httpClient.get("https://www.googleapis.com/oauth2/v2/userinfo") {
            headers {
                append(HttpHeaders.Authorization, "Bearer $token")
            }
        }

        if (response.status == HttpStatusCode.Unauthorized) {
            return null
        }
        return response.body()
    }

    @Serializable
    private data class GoogleUser(val email: String)

}
