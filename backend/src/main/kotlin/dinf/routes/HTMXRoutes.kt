package dinf.routes

import dinf.html.components.DiceFeed
import dinf.domain.DiceService
import dinf.domain.SearchQuery
import dinf.types.toPIntOrNull
import io.ktor.server.application.*
import io.ktor.http.*
import io.ktor.server.plugins.*
import io.ktor.server.resources.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.html.div
import kotlinx.html.stream.createHTML

fun Route.search(diceService: DiceService, diceFeed: DiceFeed) {
    get<HTMXResource.Search> { resource ->
        val query = SearchQuery(
            text = resource.query ?: "",
            page = resource.page.toPIntOrNull() ?: throw BadRequestException("Count can't be less than 1"),
            count = resource.count.toPIntOrNull() ?: throw BadRequestException("Count can't be less than 1")
        )
        val dices = diceService.search(query)
        val nextPage = application.href(resource.nextPage())
        call.respondText(contentType = ContentType.Text.Html.withCharset(Charsets.UTF_8)) {
            createHTML().div {
                diceFeed.component(this, dices, nextPage)
            }
        }
    }
}

fun Route.htmxDices(diceService: DiceService, diceFeed: DiceFeed) {
    get<HTMXResource.Dices> { resource ->
        val diceList = diceService.find(
            resource.page.toPIntOrNull() ?: throw BadRequestException("Page can't be less than 1"),
            resource.count.toPIntOrNull() ?: throw BadRequestException("Count can't be less than 1")
        )
        val nextPage = application.href(resource.nextPage())
        call.respondText(contentType = ContentType.Text.Html.withCharset(Charsets.UTF_8)) {
            createHTML().div {
                diceFeed.component(this, diceList, nextPage)
            }
        }
    }
}
