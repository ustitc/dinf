package dinf.plugins

import dinf.AppDeps
import dinf.html.components.DiceCard
import dinf.html.components.DiceFeed
import dinf.config.Configuration
import dinf.routes.DiceResource
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
import io.ktor.server.application.*
import io.ktor.server.html.*
import io.ktor.http.*
import io.ktor.server.http.content.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.resources.*
import io.ktor.server.resources.href
import io.ktor.server.routing.*
import io.ktor.server.webjars.*
import kotlinx.html.p

fun Application.configureRouting(
    config: Configuration,
    dependencies: AppDeps
) {
    install(Resources)

    val layout = Layout(htmxConfiguration = config.htmx)
    val baseURL = config.server.baseURL

    val newDiceURL = href(DiceResource.New())

    val diceCard = DiceCard(shareHashids = dependencies.shareHashIDs())
    val diceFeed = DiceFeed(newDiceURL = newDiceURL, diceCard = diceCard)

    install(StatusPages) {
        status(HttpStatusCode.NotFound) { call, status ->
            call.respondHtmlTemplate(layout) {
                content {
                    p {
                        +"There is nothing here"
                    }
                }
            }
        }
    }

    install(Webjars) {
        path = "assets"
    }

    routing {
        index(
            layout = layout,
            diceGet = dependencies.diceGet(),
            diceFeed = diceFeed
        )
        create(
            layout = layout,
            editHashids = dependencies.editHashIDs(),
            diceFactory = dependencies.diceFactory()
        )
        createForm(layout = layout)
        dice(
            layout = layout,
            shareHashids = dependencies.shareHashIDs(),
            dices = dependencies.dices(),
            diceMetrics = dependencies.diceMetrics()
        )
        edit(
            layout = layout,
            editHashids = dependencies.editHashIDs(),
            dices = dependencies.dices()
        )
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
    }
}
