package dinf.routes

import dinf.domain.DiceGet
import dinf.html.components.DiceFeed
import dinf.domain.DiceSearch
import dinf.types.toPInt
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.locations.*
import io.ktor.response.*
import io.ktor.routing.*
import kotlinx.html.div
import kotlinx.html.stream.createHTML

fun Route.search(diceSearch: DiceSearch, diceFeed: DiceFeed) {
    get<HTMXLocations.Search> { loc ->
        val dices = diceSearch.invoke(loc.query ?: "")

        call.respondText(contentType = ContentType.Text.Html.withCharset(Charsets.UTF_8)) {
            createHTML().div {
                diceFeed.component(this, dices)
            }
        }
    }
}

fun Route.htmxDices(diceGet: DiceGet, diceFeed: DiceFeed) {
    get<HTMXLocations.Dices> { loc ->
        val diceList = diceGet.invoke(loc.page.toPInt(), loc.count.toPInt())
        call.respondText(contentType = ContentType.Text.Html.withCharset(Charsets.UTF_8)) {
            createHTML().div {
                diceFeed.component(this, diceList)
            }
        }
    }

}
