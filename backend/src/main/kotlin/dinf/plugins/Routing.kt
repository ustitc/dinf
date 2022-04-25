package dinf.plugins

import dinf.AppDeps
import dinf.html.components.DiceCard
import dinf.html.components.DiceFeed
import dinf.config.Configuration
import dinf.html.pages.Page
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

private lateinit var layout: Layout

suspend fun ApplicationCall.respondPage(page: Page) {
    respondHtmlTemplate(layout) {
        insert(page) {}
    }
}

fun Application.configureRouting(
    config: Configuration,
    dependencies: AppDeps
) {
    install(Resources)

    layout = Layout(htmxConfiguration = config.htmx)
    val baseURL = config.server.baseURL

    val newDiceURL = href(DiceResource.New())

    val diceCard = DiceCard(publicIDFactory = dependencies.publicIDFactory())
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
            diceGet = dependencies.diceGet(),
            diceFeed = diceFeed
        )
        create(
            diceService = dependencies.diceService()
        )
        createForm()
        dice(
            publicIDFactory = dependencies.publicIDFactory(),
            diceRepository = dependencies.diceRepository(),
            diceMetricRepository = dependencies.diceMetricRepository()
        )
        edit(
            publicIDFactory = dependencies.publicIDFactory(),
            diceRepository = dependencies.diceRepository()
        )
        editForm(
            publicIDFactory = dependencies.publicIDFactory(),
            baseURL = baseURL,
            diceRepository = dependencies.diceRepository()
        )
        delete(
            editHashids = dependencies.publicIDFactory(),
            diceService = dependencies.diceService()
        )
        search(
            diceService = dependencies.diceService(),
            diceFeed = diceFeed
        )
        htmxDices(
            diceGet = dependencies.diceGet(),
            diceFeed = diceFeed
        )

        static("assets") {
            resources("js")
            resources("css")
            resources("img")
        }
    }
}
