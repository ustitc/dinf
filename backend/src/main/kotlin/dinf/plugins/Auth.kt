package dinf.plugins

import dinf.AppDeps
import dinf.auth.UserSession
import dinf.routes.LoginResource
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.resources.*
import io.ktor.server.response.*
import io.ktor.server.sessions.*
import kotlin.time.Duration

const val FORM_LOGIN_CONFIGURATION_NAME = "form-auth"
const val SESSION_LOGIN_CONFIGURATION_NAME = "session-auth"
const val FORM_LOGIN_EMAIL_FIELD = "email"
const val FORM_LOGIN_PASSWORD_FIELD = "password"

fun Application.configureAuth(appDeps: AppDeps) {
    val failedAuthURL = href(LoginResource(failed = true))

    install(Authentication) {
        val userPrincipleSvc = appDeps.userPrincipalService()
        form(FORM_LOGIN_CONFIGURATION_NAME) {
            userParamName = FORM_LOGIN_EMAIL_FIELD
            passwordParamName = FORM_LOGIN_PASSWORD_FIELD

            validate { credential ->
                userPrincipleSvc.find(credential)
            }
            challenge(failedAuthURL)
        }
        session<UserSession>(SESSION_LOGIN_CONFIGURATION_NAME) {
            validate {
                it
            }
            challenge {
                call.respondRedirect("/")
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
