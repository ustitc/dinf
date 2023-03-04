package dinf.app.html.components

import dinf.app.html.templates.RollBlock
import dinf.app.routes.DiceResource
import dinf.app.services.DiceView
import io.ktor.resources.*
import io.ktor.resources.serialization.*
import io.ktor.server.html.*
import kotlinx.html.FlowContent
import kotlinx.html.a
import kotlinx.html.div
import kotlinx.html.h3

class DiceCardComponentFactory {

    fun component(flowContent: FlowContent, dice: DiceView) {
        val location = href(ResourcesFormat(), DiceResource.ByID(dice.id))
        val id = "result-${dice.id.print()}"
        val eventName = "roll"

        flowContent.div {
            h3 {
                a(href = location) { +dice.name }
            }

            insert(RollBlock(dice.edges.map { it.value })) {
                this.id = id
                this.eventName = eventName
                this.withResultOnTop = true
            }
        }
    }
}
