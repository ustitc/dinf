package dinf.html.components

import dinf.routes.DiceLocation
import dinf.html.templates.RollButton
import dinf.domain.Dice
import dinf.domain.HashIDs
import dinf.html.templates.RollResult
import io.ktor.resources.*
import io.ktor.resources.serialization.*
import io.ktor.server.html.*
import kotlinx.html.FlowContent
import kotlinx.html.a
import kotlinx.html.div
import kotlinx.html.h3

class DiceCard(private val shareHashids: HashIDs) {

    fun component(flowContent: FlowContent, dice: Dice) {
        val hashID = shareHashids.fromID(dice.id)
        val location = href(ResourcesFormat(), DiceLocation.ByHashID(hashID))
        val id = "result-${hashID.print()}"
        val eventName = "roll"

        flowContent.div {
            h3 {
                a(href = location) { +dice.name.print() }
            }

            insert(RollResult(_id = id, dice = dice, eventName = eventName)) {
            }
            insert(RollButton(_id = id, eventName = eventName)) {
            }
        }
    }

}
