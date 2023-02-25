package dinf.auth

import dinf.db.firstOrNull
import dinf.db.getPLong
import dinf.db.setPLong
import dinf.db.sql
import dinf.domain.ID
import dinf.domain.User
import dinf.domain.UserFactory
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.serialization.Serializable

class OAuthService(
    private val httpClient: HttpClient,
    private val userFactory: UserFactory,
    private val nameSource: () -> String,
) {

    sealed class Login {
        class Ok(val userPrincipal: UserPrincipal) : Login()
        object Invalid : Login()
    }

    suspend fun login(credential: Credential.Google): Login {
        val googleUser = findUserInGoogle(credential.token) ?: return Login.Invalid
        val user = findOrCreateInDb(googleUser)
        return Login.Ok(UserPrincipal(user))
    }

    private fun findOrCreateInDb(googleUser: GoogleUser): User {
        val user = findUserInDb(googleUser)
        if (user != null) {
            return user
        }
        val createdUser = userFactory.create(nameSource.invoke())
        addLogin(createdUser, googleUser)
        return createdUser
    }

    private fun findUserInDb(googleUser: GoogleUser): User? {
        return sql(
            """
            SELECT users.id, users.name 
            FROM users
            JOIN login_oauth_google ON users.id = login_oauth_google.user
            WHERE login_oauth_google.google_id = ?
            """.trimIndent()
        ) {
            setString(1, googleUser.id)
            executeQuery().firstOrNull {
                User(
                    id = ID(getPLong("id")),
                    name = getString("name"),
                )
            }
        }
    }

    private fun addLogin(user: User, googleUser: GoogleUser) {
        sql("INSERT INTO login_oauth_google (user, google_id) VALUES (?, ?)") {
            setPLong(1, user.id.number)
            setString(2, googleUser.id)
            execute()
        }
    }

    private suspend fun findUserInGoogle(token: String): GoogleUser? {
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
    private data class GoogleUser(val id: String)

}
