package dinf.backend.routes

import dinf.backend.components.diceFeed
import dinf.domain.DiceSearch
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.locations.*
import io.ktor.response.*
import io.ktor.routing.*
import kotlinx.html.div
import kotlinx.html.stream.createHTML
import org.hashids.Hashids

fun Route.search(diceSearch: DiceSearch, shareHashids: Hashids) {
    get<HTMXLocations.Search> { loc ->
        val dices = diceSearch.forText(loc.query)

        call.respondText(contentType = ContentType.Text.Html.withCharset(Charsets.UTF_8)) {
            createHTML().div {
                diceFeed(dices, shareHashids, call)
            }
        }
    }
}
