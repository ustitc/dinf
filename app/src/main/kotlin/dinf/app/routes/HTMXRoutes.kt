package dinf.app.routes

import dinf.app.deps
import dinf.domain.SearchQuery
import dinf.types.toPIntOrNull
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.*
import io.ktor.server.resources.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.html.TagConsumer
import kotlinx.html.div
import kotlinx.html.stream.createHTML

fun Route.htmxDiceSearch() {
    get<HTMXResource.Search> { resource ->
        val query = SearchQuery(
            text = resource.query ?: "",
            page = resource.page.toPIntOrNull() ?: throw BadRequestException("Count can't be less than 1"),
            count = resource.count.toPIntOrNull() ?: throw BadRequestException("Count can't be less than 1")
        )
        val dices = deps.diceService().search(query)
        val nextPage = application.href(resource.nextPage())
        val factory = deps.diceFeedComponentFactory(call)
        call.respondHtmx {
            div {
                factory.component(this, dices, nextPage)
            }
        }
    }
}

fun Route.htmxDiceList() {
    get<HTMXResource.Dices> { resource ->
        val diceList = deps.diceService().find(
            resource.page.toPIntOrNull() ?: throw BadRequestException("Page can't be less than 1"),
            resource.count.toPIntOrNull() ?: throw BadRequestException("Count can't be less than 1")
        )
        val nextPage = application.href(resource.nextPage())
        val factory = deps.diceFeedComponentFactory(call)
        call.respondHtmx {
            div {
                factory.component(this, diceList, nextPage)
            }
        }
    }
}

private suspend fun ApplicationCall.respondHtmx(block: TagConsumer<String>.() -> String) {
    respondText(contentType = ContentType.Text.Html.withCharset(Charsets.UTF_8)) {
        createHTML().block()
    }
}
