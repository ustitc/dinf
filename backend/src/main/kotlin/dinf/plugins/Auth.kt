package dinf.plugins

import dinf.auth.Credential
import dinf.auth.EmailPasswordValidation
import dinf.auth.UserSession
import dinf.config.AppConfig
import dinf.deps
import dinf.routes.LoginResource
import dinf.routes.OAuthResource
import io.ktor.client.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.resources.*
import io.ktor.server.response.*
import io.ktor.server.sessions.*
import kotlin.time.Duration

const val FORM_LOGIN_CONFIGURATION_NAME = "form-auth"
const val SESSION_LOGIN_CONFIGURATION_NAME = "session-auth"
const val OAUTH_GOOGLE_CONFIGURATION_NAME = "oauth-google"
const val FORM_LOGIN_EMAIL_FIELD = "email"
const val FORM_LOGIN_PASSWORD_FIELD = "password"

fun Application.configureAuth(config: AppConfig, httpClient: HttpClient) {
    val failedAuthURL = href(LoginResource(failed = true))
    val googleCallback = href(OAuthResource.Google.Callback())
    val loginConfig = config.login

    install(Authentication) {
        if (loginConfig.password.enabled) {
            val authSvc = deps.emailPasswordService()
            form(FORM_LOGIN_CONFIGURATION_NAME) {
                userParamName = FORM_LOGIN_EMAIL_FIELD
                passwordParamName = FORM_LOGIN_PASSWORD_FIELD

                validate { credential ->
                    val validation = authSvc.login(
                        Credential.EmailPassword(credential)
                    )
                    when (validation) {
                        is EmailPasswordValidation.Ok -> validation.userPrincipal
                        else -> null
                    }
                }
                challenge(failedAuthURL)
            }
        }
        session<UserSession>(SESSION_LOGIN_CONFIGURATION_NAME) {
            validate {
                it
            }
            challenge {
                call.respondRedirect("/")
            }
        }

        if (loginConfig.oauth.google.enabled) {
            oauth(OAUTH_GOOGLE_CONFIGURATION_NAME) {
                urlProvider = { "${config.server.baseURL}$googleCallback" }
                providerLookup = {
                    OAuthServerSettings.OAuth2ServerSettings(
                        name = "google",
                        authorizeUrl = "https://accounts.google.com/o/oauth2/auth",
                        accessTokenUrl = "https://accounts.google.com/o/oauth2/token",
                        requestMethod = HttpMethod.Post,
                        clientId = loginConfig.oauth.google.clientId,
                        clientSecret = loginConfig.oauth.google.clientSecret,
                        defaultScopes = listOf("openid"),
                        extraAuthParameters = listOf("access_type" to "offline"),
                    )
                }
                client = httpClient
            }
        }
    }

    install(Sessions) {
        cookie<UserSession>("dinf_session", SessionStorageMemory()) {
            cookie.path = "/"
            cookie.maxAge = Duration.parse("7d")
        }
    }
}
