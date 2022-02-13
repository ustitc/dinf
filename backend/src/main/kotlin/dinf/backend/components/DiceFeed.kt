package dinf.backend.components

import dinf.backend.templates.Feed
import dinf.domain.Dice
import io.ktor.html.*
import kotlinx.html.FlowContent
import kotlinx.html.InputType
import kotlinx.html.form
import kotlinx.html.input

class DiceFeed(
    private val newDiceURL: String,
    private val diceCard: DiceCard
) {

    fun component(flowContent: FlowContent, diceList: List<Dice>) {
        flowContent.insert(Feed()) {
            diceList.forEach { dice ->
                item {
                    diceCard.component(this, dice)
                }
            }
            noContent {
                form(action = newDiceURL) {
                    input(type = InputType.submit) {
                        value = "Create new dice"
                    }
                }
            }
        }
    }
}
