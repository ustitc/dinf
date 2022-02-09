package dinf.backend.plugins

import dinf.backend.config.Configuration
import dinf.backend.routes.create
import dinf.backend.routes.createForm
import dinf.backend.routes.dice
import dinf.backend.routes.delete
import dinf.backend.routes.index
import dinf.backend.routes.edit
import dinf.backend.routes.editForm
import dinf.backend.routes.search
import dinf.backend.templates.Layout
import dinf.domain.DiceSave
import dinf.domain.DiceSearch
import dinf.domain.Dices
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.html.*
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.locations.*
import io.ktor.routing.*
import io.ktor.webjars.*
import kotlinx.html.p

fun Application.configureRouting(
    config: Configuration,
    dices: Dices,
    diceSave: DiceSave,
    diceSearch: DiceSearch
) {
    install(Locations) {
    }

    val layout = Layout(locations = locations)
    val urls = config.urls
    val shareHashids = urls.share.hashids()
    val editHashids = urls.edit.hashids()
    val baseURL = config.server.baseURL

    install(StatusPages) {
        status(HttpStatusCode.NotFound) {
            call.respondHtmlTemplate(layout) {
                content {
                    p {
                        +"There is nothing here"
                    }
                }
            }
        }
    }

    routing {
        index(layout = layout, shareHashids = shareHashids, dices = dices)
        create(layout = layout, editHashids = editHashids, diceSave = diceSave)
        createForm(layout = layout)
        dice(layout = layout, shareHashids = shareHashids, baseURL = baseURL, dices = dices)
        edit(layout = layout, editHashids = editHashids, dices = dices)
        editForm(
            layout = layout,
            shareHashids = shareHashids,
            editHashids = editHashids,
            baseURL = baseURL,
            dices = dices
        )
        delete(layout = layout, editHashids = editHashids, dices = dices)
        search(diceSearch = diceSearch, shareHashids = shareHashids)

        static("assets") {
            resources("js")
            resources("css")
            resources("img")
        }
        install(Webjars) {
            path = "assets"
        }
    }
}
