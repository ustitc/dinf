package dinf.backend.plugins

import dinf.backend.config.Configuration
import dinf.backend.routes.create
import dinf.backend.routes.createForm
import dinf.backend.routes.dice
import dinf.backend.routes.index
import dinf.backend.routes.edit
import dinf.backend.routes.editForm
import dinf.backend.templates.Layout
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.content.*
import io.ktor.locations.*
import io.ktor.routing.*
import io.ktor.webjars.*

fun Application.configureRouting(config: Configuration) {

    install(Locations) {
    }

    routing {
        val layout = Layout(
            baseURL = config.server.baseURL,
            locations = application.locations
        )
        val urls = config.urls
        val shareHashids = urls.share.hashids()
        val editHashids = urls.edit.hashids()

        index(layout = layout, shareHashids = shareHashids)
        create(layout = layout, editHashids = editHashids)
        createForm(layout = layout)
        dice(layout = layout, shareHashids = shareHashids)
        edit(layout = layout, editHashids = editHashids)
        editForm(layout = layout, shareHashids = shareHashids, editHashids = editHashids)

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
