package dinf.app.html.pages

import dinf.app.html.components.DiceFeedComponentFactory
import dinf.app.html.templates.LayoutTemplate
import dinf.app.html.templates.SearchBarTemplate
import dinf.app.services.DiceView
import io.ktor.server.html.*

class MainPage(
    private val searchURL: String,
    private val nextDicePageURL: String,
    private val diceList: List<DiceView>,
    private val diceFeed: DiceFeedComponentFactory,
) : Page {

    override fun LayoutTemplate.apply() {
        content {
            insert(SearchBarTemplate(searchURL)) {
                initialContent {
                    diceFeed.component(this, diceList, nextDicePageURL)
                }
            }
        }
    }
}
