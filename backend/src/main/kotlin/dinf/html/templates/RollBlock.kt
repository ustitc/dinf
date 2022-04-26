package dinf.html.templates

import io.ktor.server.html.*
import kotlinx.html.FlowContent

@Suppress("MemberVisibilityCanBePrivate")
class RollBlock(private var rollValues: List<String>) : Template<FlowContent> {

    val button = TemplatePlaceholder<RollButton>()
    val result = TemplatePlaceholder<RollResult>()

    var id = "result"
    var eventName = "roll"
    var withResultOnTop: Boolean = false
    var defaultValue: String = "Dice is empty, add some values to start rolling!"

    override fun FlowContent.apply() {
        if (rollValues.isEmpty()) {
            rollValues = listOf(defaultValue)
        }
        if (withResultOnTop) {
            insert(RollResult(id, eventName, rollValues), result)
            insert(RollButton(id, eventName), button)
        } else {
            insert(RollButton(id, eventName), button)
            insert(RollResult(id, eventName, rollValues), result)
        }
    }
}
