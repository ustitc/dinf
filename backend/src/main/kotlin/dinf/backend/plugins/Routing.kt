package dinf.backend.plugins

import dinf.backend.templates.Layout
import dinf.backend.routes.createDice
import dinf.backend.routes.dices
import dinf.backend.routes.index
import dinf.backend.routes.create
import dinf.backend.routes.dice
import dinf.backend.routes.createForm
import dinf.backend.routes.login
import dinf.backend.routes.loginForm
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.locations.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.webjars.*

fun Application.configureRouting() {

    install(Locations) {
    }

    routing {
        val layout = Layout(application.locations)

        dices()
        createDice()
        index(layout)
        create(layout)
        createForm(layout)
        dice(layout)
        loginForm(layout)
        login()
        install(StatusPages) {
            exception<AuthenticationException> { cause ->
                call.respond(HttpStatusCode.Unauthorized)
            }
            exception<AuthorizationException> { cause ->
                call.respond(HttpStatusCode.Forbidden)
            }
        }
        static("assets") {
            resources("js")
            resources("img")
        }
        install(Webjars) {
            path = "assets"
        }
    }
}

class AuthenticationException : RuntimeException()
class AuthorizationException : RuntimeException()
