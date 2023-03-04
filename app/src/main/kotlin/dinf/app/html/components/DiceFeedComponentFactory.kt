package dinf.app.html.components

import dev.ustits.htmx.HTMX_INDICATOR
import dev.ustits.htmx.HtmxSwap
import dev.ustits.htmx.htmx
import dinf.app.html.templates.DiceCardTemplate
import dinf.app.html.templates.FeedTemplate
import dinf.app.routes.DiceResource
import dinf.app.services.DiceView
import io.ktor.resources.*
import io.ktor.resources.serialization.*
import io.ktor.server.html.*
import kotlinx.html.FlowContent
import kotlinx.html.InputType
import kotlinx.html.a
import kotlinx.html.button
import kotlinx.html.div
import kotlinx.html.form
import kotlinx.html.hr
import kotlinx.html.id
import kotlinx.html.input

class DiceFeedComponentFactory(private val showAddButton: Boolean) {

    private val loadBlockID = "pagination-block-load"

    fun component(flowContent: FlowContent, diceList: List<DiceView>, nextPageURL: String? = null) {
        val newDiceUrl = href(ResourcesFormat(), DiceResource.New())
        flowContent.insert(FeedTemplate()) {
            diceList.forEach { dice ->
                val diceLocation = href(ResourcesFormat(), DiceResource.ByID(dice.id))
                item {
                    insert(DiceCardTemplate(dice)) {
                        name {
                            a(href = diceLocation) { +dice.name }
                        }
                    }
                    hr { }
                }
            }
            pagination {
                if (nextPageURL != null) {
                    val elementID = "pagination-block"
                    div {
                        id = elementID
                        button(classes = "outline") {
                            htmx {
                                hxGet(nextPageURL)
                                hxSwap(HtmxSwap.OUTER_HTML)
                                hxIndicator("#$loadBlockID")
                                hxTarget("#$elementID")
                            }
                            +"Give me more!"
                        }
                        loadingComponent(
                            text = "Wait a second...",
                            id = loadBlockID,
                            classes = HTMX_INDICATOR
                        )
                    }
                }
            }
            noContent {
                if (showAddButton) {
                    form(action = newDiceUrl) {
                        input(type = InputType.submit) {
                            value = "Create new dice"
                        }
                    }
                }
            }
        }
    }
}
