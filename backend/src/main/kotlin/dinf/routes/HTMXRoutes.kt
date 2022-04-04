package dinf.routes

import dinf.domain.DiceGet
import dinf.html.components.DiceFeed
import dinf.domain.DiceSearch
import dinf.domain.SearchQuery
import dinf.types.toPIntOrNull
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.locations.*
import io.ktor.response.*
import io.ktor.routing.*
import kotlinx.html.div
import kotlinx.html.stream.createHTML

fun Route.search(diceSearch: DiceSearch, diceFeed: DiceFeed) {
    get<HTMXLocations.Search> { loc ->
        val query = SearchQuery(
            text = loc.query ?: "",
            page = loc.page.toPIntOrNull() ?: throw BadRequestException("Count can't be less than 1"),
            count = loc.count.toPIntOrNull() ?: throw BadRequestException("Count can't be less than 1")
        )
        val dices = diceSearch.invoke(query)
        val nextPage = application.locations.href(loc.nextPage())
        call.respondText(contentType = ContentType.Text.Html.withCharset(Charsets.UTF_8)) {
            createHTML().div {
                diceFeed.component(this, dices, nextPage)
            }
        }
    }
}

fun Route.htmxDices(diceGet: DiceGet, diceFeed: DiceFeed) {
    get<HTMXLocations.Dices> { loc ->
        val diceList = diceGet.invoke(
            loc.page.toPIntOrNull() ?: throw BadRequestException("Page can't be less than 1"),
            loc.count.toPIntOrNull() ?: throw BadRequestException("Count can't be less than 1")
        )
        val nextPage = application.locations.href(loc.nextPage())
        call.respondText(contentType = ContentType.Text.Html.withCharset(Charsets.UTF_8)) {
            createHTML().div {
                diceFeed.component(this, diceList, nextPage)
            }
        }
    }
}
