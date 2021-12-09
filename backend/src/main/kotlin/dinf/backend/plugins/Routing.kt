package dinf.backend.plugins

import dinf.backend.routes.create
import dinf.backend.routes.createForm
import dinf.backend.routes.dice
import dinf.backend.routes.index
import dinf.backend.templates.Layout
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.content.*
import io.ktor.locations.*
import io.ktor.routing.*
import io.ktor.webjars.*

fun Application.configureRouting() {

    install(Locations) {
    }

    routing {
        val layout = Layout(application.locations)

        index(layout)
        create(layout)
        createForm(layout)
        dice(layout)
        install(StatusPages) {
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
