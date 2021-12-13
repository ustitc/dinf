package dinf.backend.routes

import dinf.backend.DBDices
import dinf.backend.HashID
import dinf.backend.HashSerialNumber
import dinf.backend.templates.BulmaColor
import dinf.backend.templates.BulmaMessage
import dinf.backend.templates.CreateDiceForm
import dinf.backend.templates.Feed
import dinf.backend.templates.Form
import dinf.backend.templates.DiceView
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
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import kotlinx.html.a
import kotlinx.html.div
import kotlinx.html.p
import org.hashids.Hashids

private val dices = DBDices()

fun Route.index(layout: Layout, hashids: Hashids) {
    get("/") {
        val diceViews = dices.flow()
            .map {
                val id = HashID(it.serialNumber, hashids)
                DiceView(it, id)
            }
            .toList()

        call.respondHtmlTemplate(layout) {
            content {
                insert(Feed()) {
                    diceViews.map { view ->
                        card {
                            content {
                                insert(view) {
                                    footer {
                                        val location = call.locations.href(DiceLocation.ID(id))
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

fun Route.create(layout: Layout, hashids: Hashids) {
    post<DiceLocation.New> {
        val params = call.receiveParameters()
        val dice = HTMLParamsDice
            .fromParametersOrNull(params)
            ?.let { dices.create(it) }
        if (dice != null) {
            val id = HashID(dice, hashids)
            val url = call.locations.href(DiceLocation.ID(id))
            call.respondRedirect(url)
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

fun Route.dice(layout: Layout, hashids: Hashids) {
    get<DiceLocation.ID> {
        val diceID = ID.Simple(it.id)
        val serial = HashSerialNumber(it.id, hashids)
        val dice = dices.dice(serial)
        if (dice == null) {
            call.respond(status = HttpStatusCode.NotFound, "")
        } else {
            call.respondHtmlTemplate(layout) {
                content {
                    insert(DiceView(dice, diceID)) {
                        header {
                            val id = HashID(dice, hashids)
                            val uri = call.locations.href(DiceLocation.ID(id))
                            val url = "$baseURL$uri"
                            div("block") {
                                +"Share url: "
                                a(href = url) { +url }
                            }
                        }
                    }
                }
            }
        }
    }
}
