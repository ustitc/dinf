package dinf.routes

import dinf.domain.DiceGet
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
    get<HTMXResource.Search> { loc ->
        val query = SearchQuery(
            text = loc.query ?: "",
            page = loc.page.toPIntOrNull() ?: throw BadRequestException("Count can't be less than 1"),
            count = loc.count.toPIntOrNull() ?: throw BadRequestException("Count can't be less than 1")
        )
        val dices = diceService.search(query)
        val nextPage = application.href(loc.nextPage())
        call.respondText(contentType = ContentType.Text.Html.withCharset(Charsets.UTF_8)) {
            createHTML().div {
                diceFeed.component(this, dices, nextPage)
            }
        }
    }
}

fun Route.htmxDices(diceGet: DiceGet, diceFeed: DiceFeed) {
    get<HTMXResource.Dices> { loc ->
        val diceList = diceGet.invoke(
            loc.page.toPIntOrNull() ?: throw BadRequestException("Page can't be less than 1"),
            loc.count.toPIntOrNull() ?: throw BadRequestException("Count can't be less than 1")
        )
        val nextPage = application.href(loc.nextPage())
        call.respondText(contentType = ContentType.Text.Html.withCharset(Charsets.UTF_8)) {
            createHTML().div {
                diceFeed.component(this, diceList, nextPage)
            }
        }
    }
}
