package dinf.backend.plugins

import dinf.backend.routes.articles
import io.ktor.routing.*
import io.ktor.http.*
import io.ktor.features.*
import io.ktor.locations.*
import io.ktor.application.*
import io.ktor.response.*

fun Application.configureRouting() {

    install(Locations) {
    }

    routing {
        articles()
        install(StatusPages) {
            exception<AuthenticationException> { cause ->
                call.respond(HttpStatusCode.Unauthorized)
            }
            exception<AuthorizationException> { cause ->
                call.respond(HttpStatusCode.Forbidden)
            }

        }
    }
}

class AuthenticationException : RuntimeException()
class AuthorizationException : RuntimeException()
