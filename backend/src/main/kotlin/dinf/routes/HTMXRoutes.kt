package dinf.routes

import dinf.html.components.DiceFeed
import dinf.domain.DiceSearch
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.locations.*
import io.ktor.response.*
import io.ktor.routing.*
import kotlinx.html.div
import kotlinx.html.stream.createHTML

fun Route.search(diceSearch: DiceSearch, diceFeed: DiceFeed) {
    get<HTMXLocations.Search> { loc ->
        val dices = diceSearch.forText(loc.query ?: "")

        call.respondText(contentType = ContentType.Text.Html.withCharset(Charsets.UTF_8)) {
            createHTML().div {
                diceFeed.component(this, dices)
            }
        }
    }
}
