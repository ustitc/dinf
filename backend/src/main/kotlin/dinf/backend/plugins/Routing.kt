package dinf.backend.plugins

import dinf.backend.config.Configuration
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
import org.hashids.Hashids

fun Application.configureRouting(config: Configuration) {

    install(Locations) {
    }

    routing {
        val layout = Layout(
            baseURL = config.server.baseURL,
            locations = application.locations
        )
        val urls = config.urls
        val hashids = Hashids(urls.salt, urls.publicLength)

        index(layout, hashids)
        create(layout, hashids)
        createForm(layout)
        dice(layout, hashids)
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
