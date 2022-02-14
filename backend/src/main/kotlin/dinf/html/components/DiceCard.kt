package dinf.html.components

import dinf.adapters.HashID
import dinf.routes.DiceLocation
import dinf.html.templates.RollButton
import dinf.domain.Dice
import io.ktor.html.*
import io.ktor.locations.*
import kotlinx.html.FlowContent
import kotlinx.html.a
import kotlinx.html.h2
import kotlinx.html.section
import org.hashids.Hashids

class DiceCard(private val shareHashids: Hashids, private val locations: Locations) {

    fun component(flowContent: FlowContent, dice: Dice) {
        val id = HashID(dice.serialNumber, shareHashids)
        val location = locations.href(DiceLocation.ID(id))

        flowContent.section {
            h2 {
                a(href = location) { +dice.name.nbString.toString() }
            }
            insert(RollButton(dice)) {
                resultTagID = "result-${id.print()}"
            }
        }
    }

}
