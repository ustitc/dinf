package dinf.backend.components

import dinf.backend.HashID
import dinf.backend.routes.DiceLocation
import dinf.backend.templates.Feed
import dinf.backend.templates.RollButton
import dinf.domain.Dice
import io.ktor.application.*
import io.ktor.html.*
import io.ktor.locations.*
import kotlinx.html.FlowContent
import kotlinx.html.a
import kotlinx.html.h2
import org.hashids.Hashids

fun FlowContent.diceFeed(dices: List<Dice>, shareHashids: Hashids, call: ApplicationCall) {
    insert(Feed()) {
        dices.forEach { dice ->
            card {
                diceCard(dice, shareHashids, call)
            }
        }
    }
}

fun FlowContent.diceCard(dice: Dice, shareHashids: Hashids, call: ApplicationCall) {
    val id = HashID(dice.serialNumber, shareHashids)
    val location = call.locations.href(DiceLocation.ID(id))

    h2 {
        a(href = location) { +dice.name.nbString.toString() }
    }
    insert(RollButton(dice)) {
        resultTagID = "result-${id.print()}"
    }
}


