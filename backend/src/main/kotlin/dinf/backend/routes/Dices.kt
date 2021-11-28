package dinf.backend.routes

import dinf.api.APIDice
import dinf.domain.Dice
import dinf.domain.Dices
import dinf.domain.Edges
import dinf.domain.Name
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.locations.*
import io.ktor.locations.post
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.Route
import kotlinx.coroutines.flow.toList

@Location("/dices")
class DiceLocation

private val dices = Dices.Stub(
    mutableListOf(
        Dice.Simple(
            name = Name.Stub("Dices"),
            edges = Edges.Simple("d4", "d6", "d8", "d10", "d12", "d20", "d100")
        ),
        Dice.Simple(
            name = Name.Stub("Colors"),
            edges = Edges.Simple("red", "green", "blue", "purple", "cyan", "yellow")
        ),
        Dice.Simple(
            name = "D6",
            edges = Edges.Simple(1, 2, 3, 4, 5, 6)
        )
    )
)

fun Route.dices() {
    get<DiceLocation> {
        val diceList = dices.flow().toList()
        val response: List<APIDice> = diceList.map { APIDice(it) }
        call.respond(
            response
        )
    }
}

fun Route.createDice() {
    post<DiceLocation> {
        val dice = call.receive<APIDice>()
        dices.create(dice)
        call.response.status(HttpStatusCode.OK)
    }
}
