package dinf.backend.routes

import dinf.api.APIDice
import dinf.backend.templates.Feed
import dinf.backend.templates.Form
import dinf.backend.templates.Layout
import dinf.domain.Dice
import dinf.domain.Dices
import dinf.domain.Edges
import dinf.domain.ID
import io.ktor.application.*
import io.ktor.html.*
import io.ktor.http.*
import io.ktor.locations.*
import io.ktor.locations.post
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import kotlinx.coroutines.flow.toList
import kotlinx.html.InputType
import kotlinx.html.button
import kotlinx.html.div
import kotlinx.html.h1
import kotlinx.html.h2
import kotlinx.html.id
import kotlinx.html.input
import kotlinx.html.onClick
import kotlinx.html.p
import kotlinx.html.textArea

private val dices = Dices.Stub(
    mutableListOf(
        Dice.Simple(
            id = ID.Simple(1),
            name = "Dices",
            edges = Edges.Simple("d4", "d6", "d8", "d10", "d12", "d20", "d100")
        ),
        Dice.Simple(
            id = ID.Simple(2),
            name = "Colors",
            edges = Edges.Simple("red", "green", "blue", "purple", "cyan", "yellow")
        ),
        Dice.Simple(
            id = ID.Simple(3),
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

fun Route.index(layout: Layout) {
    get("/") {
        val diceList = dices.flow().toList()

        call.respondHtmlTemplate(layout) {
            content {
                insert(Feed()) {
                    diceList.mapIndexed { i, dice ->
                        val rollValues = dice.edges
                            .stringList.joinToString(prefix = "[", postfix = "]") { "\"$it\"" }
                        val diceID = "result-$i"

                        card {
                            content {
                                h1(classes = "title") { +dice.name.nbString.toString() }
                                h2(classes = "subtitle") { +"by <unknown>" }
                                div(classes = "block") {
                                    button(classes = "button is-primary") {
                                        onClick = "roll($rollValues, \"$diceID\")"
                                        +"Roll"
                                    }
                                }
                                div(classes = "block") {
                                    this.id = diceID
                                }
                            }
                        }
                    }
                }

            }
        }
    }
}

fun Route.createForm(layout: Layout) {
    get<DiceLocation.New> {
        call.respondHtmlTemplate(layout) {
            content {
                insert(Form(newDiceURL)) {
                    field {
                        name = "Name"
                        control {
                            input(classes = "input", name = "name", type = InputType.text)
                        }
                    }
                    field {
                        name = "Edges"
                        help = "Each value must be on new line"
                        control {
                            textArea(classes = "textarea") {
                                name = "edges"
                            }
                        }
                    }
                    submit {
                        value = "Save"
                    }
                }
            }
        }
    }
}

fun Route.create(layout: Layout) {
    post<DiceLocation.New> {
        val params = call.receiveParameters()
        val name = params["name"]!!
        val edges = params["edges"]!!.lines().filter { it.isNotBlank() }
        dices.create(
            Dice.Simple(
                name = name,
                edges = Edges.Simple(edges)
            )
        )
        call.respondHtmlTemplate(layout) {
            content {
                p { +"Created" }
            }
        }
    }
}
