package dinf.backend.routes

import dinf.api.APIDice
import dinf.backend.DBDices
import dinf.backend.templates.Feed
import dinf.backend.templates.Form
import dinf.backend.templates.HTMLDice
import dinf.backend.templates.Layout
import dinf.domain.Dice
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
import kotlinx.html.a
import kotlinx.html.div
import kotlinx.html.input
import kotlinx.html.p
import kotlinx.html.textArea

private val dices = DBDices()

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
                    diceList.map { dice ->
                        card {
                            content {
                                insert(HTMLDice(dice)) {}
                                val location = call.locations.href(DiceLocation.ID(id = dice.id.nbString.toString()))
                                div("block") {
                                    a(href = location) { +"Open" }
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

fun Route.dice(layout: Layout) {
    get<DiceLocation.ID> {
        val diceID = ID.Simple(it.id)
        val dice = dices.dice(diceID)
        if (dice == null) {
            call.respond(status = HttpStatusCode.NotFound, "")
        } else {
            call.respondHtmlTemplate(layout) {
                content {
                    insert(HTMLDice(dice)) {}
                }
            }
        }
    }
}
