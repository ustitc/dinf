package dinf.backend.routes

import dinf.api.APIDice
import dinf.backend.DBDices
import dinf.backend.templates.BulmaColor
import dinf.backend.templates.BulmaMessage
import dinf.backend.templates.CreateDiceForm
import dinf.backend.templates.Feed
import dinf.backend.templates.Form
import dinf.backend.templates.HTMLDice
import dinf.backend.templates.Layout
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
import kotlinx.html.a
import kotlinx.html.div
import kotlinx.html.p

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
                val form = Form(newDiceURL)
                insert(CreateDiceForm(form)) {}
            }
        }
    }
}

fun Route.create(layout: Layout) {
    post<DiceLocation.New> {
        val params = call.receiveParameters()
        val dice = HTMLParamsDice.fromParametersOrNull(params)
        if (dice != null) {
            dices.create(dice)
            call.respondHtmlTemplate(layout) {
                content {
                    p { +"Created" }
                }
            }
        } else {
            call.respondHtmlTemplate(layout) {
                content {
                    insert(BulmaMessage()) {
                        color = BulmaColor.IS_WARNING
                        body {
                            p { +"Please specify name and edges" }
                        }
                    }

                    val form = Form(newDiceURL)
                    insert(CreateDiceForm(form)) {}
                }
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
