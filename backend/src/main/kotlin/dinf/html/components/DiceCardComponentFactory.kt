package dinf.html.components

import dinf.routes.DiceResource
import dinf.domain.Dice
import dinf.domain.PublicIDFactory
import dinf.html.templates.RollBlock
import io.ktor.resources.*
import io.ktor.resources.serialization.*
import io.ktor.server.html.*
import kotlinx.html.FlowContent
import kotlinx.html.a
import kotlinx.html.div
import kotlinx.html.h3

class DiceCardComponentFactory(private val publicIDFactory: PublicIDFactory) {

    fun component(flowContent: FlowContent, dice: Dice) {
        val hashID = publicIDFactory.fromID(dice.id)
        val location = href(ResourcesFormat(), DiceResource.ByID(hashID))
        val id = "result-${hashID.print()}"
        val eventName = "roll"

        flowContent.div {
            h3 {
                a(href = location) { +dice.name.print() }
            }

            insert(RollBlock(dice.edges.toStringList())) {
                this.id = id
                this.eventName = eventName
                this.withResultOnTop = true
            }
        }
    }

}
