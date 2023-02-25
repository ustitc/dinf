package dinf.app.html.pages

import dinf.domain.Dice
import dinf.app.html.components.DiceFeedComponentFactory
import dinf.app.html.templates.Layout
import dinf.app.html.templates.SearchBar
import io.ktor.server.html.*

class MainPage(
    private val searchURL: String,
    private val nextDicePageURL: String,
    private val diceList: List<Dice>,
    private val diceFeed: DiceFeedComponentFactory,
) : Page {

    override fun Layout.apply() {
        content {
            insert(SearchBar(searchURL)) {
                initialContent {
                    diceFeed.component(this, diceList, nextDicePageURL)
                }
            }
        }
    }
}
