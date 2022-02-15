package dinf.html.templates

import dev.ustits.htmx.HTMX_INDICATOR
import dev.ustits.htmx.hxGet
import dev.ustits.htmx.hxIndicator
import dev.ustits.htmx.hxTarget
import dev.ustits.htmx.hxTrigger
import io.ktor.html.*
import kotlinx.html.FlowContent
import kotlinx.html.InputType
import kotlinx.html.a
import kotlinx.html.div
import kotlinx.html.id
import kotlinx.html.input

class SearchBar(private val searchApiURL: String): Template<FlowContent> {

    private val searchResultID = "search-results"
    private val loadBlockID = "load-block"

    val initialContent = Placeholder<FlowContent>()

    override fun FlowContent.apply() {
        input(type = InputType.search, name = "query") {
            placeholder = "Search dices..."
            hxGet = searchApiURL
            hxTarget = "#$searchResultID"
            hxTrigger = "keyup changed delay:300ms"
            hxIndicator = "#$loadBlockID"
        }
        a(href = "#", classes = HTMX_INDICATOR) {
            id = loadBlockID
            attributes["aria-busy"] = "true"
            +"Searching for dices..."
        }
        div {
            id = searchResultID
            insert(initialContent)
        }
    }
}
