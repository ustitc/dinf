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

private val badPageParameterException = BadRequestException("Page can't be less than 1")
private val badCountParameterException = BadRequestException("Count can't be less than 1")

fun Route.htmxDiceSearch() {
    get<HTMXResource.Search> { resource ->
        val query = SearchQuery(
            text = resource.query ?: "",
            page = resource.page.toPIntOrNull() ?: throw badPageParameterException,
            count = resource.count.toPIntOrNull() ?: throw badCountParameterException
        )
        val dices = deps.diceViewService().search(query)
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
        val diceList = deps.diceViewService().find(
            resource.page.toPIntOrNull() ?: throw badPageParameterException,
            resource.count.toPIntOrNull() ?: throw badCountParameterException
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
