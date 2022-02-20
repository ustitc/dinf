package dinf.html.components

import dinf.adapters.HashID
import dinf.routes.DiceLocation
import dinf.html.templates.RollButton
import dinf.domain.Dice
import dinf.html.templates.RollResult
import io.ktor.html.*
import io.ktor.locations.*
import kotlinx.html.FlowContent
import kotlinx.html.a
import kotlinx.html.div
import kotlinx.html.h3
import org.hashids.Hashids

class DiceCard(private val shareHashids: Hashids, private val locations: Locations) {

    fun component(flowContent: FlowContent, dice: Dice) {
        val hashID = HashID(dice.serialNumber, shareHashids)
        val location = locations.href(DiceLocation.ID(hashID))
        val id = "result-${hashID.print()}"
        val eventName = "roll"

        flowContent.div {
            h3 {
                a(href = location) { +dice.name.nbString.toString() }
            }

            insert(RollResult(_id = id, dice = dice, eventName = eventName)) {
            }
            insert(RollButton(_id = id, eventName = eventName)) {
            }
        }
    }

}
