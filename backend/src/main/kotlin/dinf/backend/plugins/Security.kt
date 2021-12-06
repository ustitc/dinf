package dinf.backend.plugins

import dinf.backend.routes.LoginLocation
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.locations.*
import io.ktor.response.*
import io.ktor.sessions.*
import kotlin.collections.set

const val LOGIN_FORM_AUTH = "login-form"

fun Application.configureSecurity() {
    data class MySession(val count: Int = 0)
    install(Sessions) {
        cookie<MySession>("MY_SESSION") {
            cookie.extensions["SameSite"] = "lax"
        }
    }

    install(Authentication) {
        form(LOGIN_FORM_AUTH) {
            userParamName = "username"
            passwordParamName = "password"
            validate { credentials ->
                println(credentials)
                if(credentials.name == "dinf" && credentials.password == "dinf") {
                    UserIdPrincipal(credentials.name)
                } else {
                    null
                }
            }
            challenge {
                val url = call.locations.href(LoginLocation.Form(fail = true))
                call.respondRedirect(url)
            }
        }
    }
}
