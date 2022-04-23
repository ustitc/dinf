package dinf.plugins

import dinf.AppDeps
import dinf.html.components.DiceCard
import dinf.html.components.DiceFeed
import dinf.config.Configuration
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
import dinf.routes.htmxDices
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
    dependencies: AppDeps
) {
    install(Locations) {
    }

    val layout = Layout(locations = locations, htmxConfiguration = config.htmx)
    val urls = config.urls
    val baseURL = config.server.baseURL

    val newDiceURL = locations.href(DiceLocation.New())

    val diceCard = DiceCard(shareHashids = dependencies.shareHashIDs(), locations = locations)
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
        index(layout = layout, diceGet = dependencies.diceGet(), diceFeed = diceFeed)
        create(layout = layout, editHashids = dependencies.editHashIDs(), diceSave = dependencies.diceSave())
        createForm(layout = layout)
        dice(
            layout = layout,
            shareHashids = dependencies.shareHashIDs(),
            dices = dependencies.dices(),
            diceMetrics = dependencies.diceMetrics()
        )
        edit(layout = layout, editHashids = dependencies.editHashIDs(), dices = dependencies.dices())
        editForm(
            layout = layout,
            editHashids = dependencies.editHashIDs(),
            baseURL = baseURL,
            dices = dependencies.dices()
        )
        delete(
            layout = layout,
            editHashids = dependencies.editHashIDs(),
            dices = dependencies.dices(),
            diceDelete = dependencies.diceDelete()
        )
        search(diceSearch = dependencies.diceSearch(), diceFeed = diceFeed)
        htmxDices(diceGet = dependencies.diceGet(), diceFeed = diceFeed)

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
