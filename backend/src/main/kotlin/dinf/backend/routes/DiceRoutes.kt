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
import dinf.backend.templates.URLBlock
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

fun Route.index(layout: Layout, shareHashids: Hashids) {
    get("/") {
        val diceViews = dices.flow()
            .map {
                val id = HashID(it.serialNumber, shareHashids)
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

fun Route.create(layout: Layout, editHashids: Hashids) {
    post<DiceLocation.New> {
        val params = call.receiveParameters()
        val dice = HTMLParamsDice
            .fromParametersOrNull(params)
            ?.let { dices.create(it) }
        if (dice != null) {
            val id = HashID(dice, editHashids)
            val url = call.locations.href(DiceLocation.Edit(id))
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

fun Route.dice(layout: Layout, shareHashids: Hashids) {
    get<DiceLocation.ID> {
        val diceID = ID.Simple(it.id)
        val serial = HashSerialNumber(it.id, shareHashids)
        val dice = dices.dice(serial)
        if (dice == null) {
            call.respond(status = HttpStatusCode.NotFound, "")
        } else {
            call.respondHtmlTemplate(layout) {
                content {
                    insert(DiceView(dice, diceID)) {
                        header {
                            val shareURL = DiceLocation.ID(
                                HashID(dice, shareHashids)
                            ).url(baseURL, call)
                            insert(URLBlock(shareURL)) {
                                text = "Share url: "
                            }
                        }
                    }
                }
            }
        }
    }
}

fun Route.edit(layout: Layout, shareHashids: Hashids, editHashids: Hashids) {
    get<DiceLocation.Edit> {
        val diceID = ID.Simple(it.id)
        val serial = HashSerialNumber(it.id, editHashids)
        val dice = dices.dice(serial)
        if (dice == null) {
            call.respond(status = HttpStatusCode.NotFound, "")
        } else {
            call.respondHtmlTemplate(layout) {
                content {
                    insert(DiceView(dice, diceID)) {
                        header {
                            val shareURL = DiceLocation.ID(
                                HashID(dice, shareHashids)
                            ).url(baseURL, call)
                            insert(URLBlock(shareURL)) {
                                text = "Share url: "
                            }

                            val editURL = DiceLocation.Edit(
                                HashID(dice, editHashids)
                            ).url(baseURL, call)
                            insert(URLBlock(editURL)) {
                                text = "Edit url: "
                            }
                        }
                    }
                }
            }
        }
    }
}

