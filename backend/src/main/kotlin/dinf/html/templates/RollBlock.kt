package dinf.html.templates

import dinf.domain.Dice
import io.ktor.server.html.*
import kotlinx.html.FlowContent

@Suppress("MemberVisibilityCanBePrivate")
class RollBlock(private val dice: Dice) : Template<FlowContent> {

    private val id = "result"
    private val eventName = "roll"

    val button = TemplatePlaceholder<RollButton>()
    val result = TemplatePlaceholder<RollResult>()

    override fun FlowContent.apply() {
        insert(RollButton(id, eventName), button)
        insert(RollResult(id, eventName, dice), result)
    }
}
