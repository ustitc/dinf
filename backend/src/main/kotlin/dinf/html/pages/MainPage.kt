package dinf.html.pages

import dinf.domain.Dice
import dinf.html.components.DiceFeed
import dinf.html.templates.Layout
import dinf.html.templates.SearchBar
import io.ktor.server.html.*

class MainPage(
    private val searchURL: String,
    private val nextDicePageURL: String,
    private val diceList: List<Dice>,
    private val diceFeed: DiceFeed,
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
