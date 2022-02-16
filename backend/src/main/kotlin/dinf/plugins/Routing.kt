package dinf.plugins

import dinf.html.components.DiceCard
import dinf.html.components.DiceFeed
import dinf.config.Configuration
import dinf.domain.DiceMetrics
import dinf.routes.DiceLocation
import dinf.routes.create
import dinf.routes.createForm
import dinf.routes.dice
import dinf.routes.delete
import dinf.routes.index
import dinf.routes.edit
import dinf.routes.editForm
import dinf.routes.search
import dinf.html.templates.Layout
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
    diceMetrics: DiceMetrics,
    diceSearch: DiceSearch
) {
    install(Locations) {
    }

    val layout = Layout(locations = locations, htmxConfiguration = config.htmx)
    val urls = config.urls
    val shareHashids = urls.share.hashids()
    val editHashids = urls.edit.hashids()
    val baseURL = config.server.baseURL

    val newDiceURL = locations.href(DiceLocation.New())

    val diceCard = DiceCard(shareHashids = shareHashids, locations = locations)
    val diceFeed = DiceFeed(newDiceURL = newDiceURL, diceCard = diceCard)

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
        index(layout = layout, dices = dices, diceFeed = diceFeed)
        create(layout = layout, editHashids = editHashids, diceSave = diceSave)
        createForm(layout = layout)
        dice(
            layout = layout,
            shareHashids = shareHashids,
            baseURL = baseURL,
            dices = dices,
            diceMetrics = diceMetrics
        )
        edit(layout = layout, editHashids = editHashids, dices = dices)
        editForm(
            layout = layout,
            shareHashids = shareHashids,
            editHashids = editHashids,
            baseURL = baseURL,
            dices = dices
        )
        delete(layout = layout, editHashids = editHashids, dices = dices)
        search(diceSearch = diceSearch, diceFeed = diceFeed)

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
