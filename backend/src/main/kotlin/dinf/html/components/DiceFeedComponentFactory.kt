package dinf.html.components

import dev.ustits.htmx.HTMX_INDICATOR
import dev.ustits.htmx.HtmxSwap
import dev.ustits.htmx.htmx
import dinf.html.templates.Feed
import dinf.domain.Dice
import io.ktor.server.html.*
import kotlinx.html.FlowContent
import kotlinx.html.InputType
import kotlinx.html.button
import kotlinx.html.div
import kotlinx.html.form
import kotlinx.html.hr
import kotlinx.html.id
import kotlinx.html.input

class DiceFeedComponentFactory(
    private val newDiceURL: String,
    private val diceCardComponentFactory: DiceCardComponentFactory,
    private val showAddButton: Boolean
) {

    private val loadBlockID = "pagination-block-load"

    fun component(flowContent: FlowContent, diceList: List<Dice>, nextPageURL: String? = null) {
        flowContent.insert(Feed()) {
            diceList.forEach { dice ->
                item {
                    diceCardComponentFactory.component(this, dice)
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
                    form(action = newDiceURL) {
                        input(type = InputType.submit) {
                            value = "Create new dice"
                        }
                    }
                }
            }
        }
    }
}
