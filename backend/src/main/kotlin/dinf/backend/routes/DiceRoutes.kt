package dinf.backend.routes

import dinf.backend.DBDices
import dinf.backend.HashID
import dinf.backend.templates.DiceForm
import dinf.backend.templates.DiceView
import dinf.backend.templates.Feed
import dinf.backend.templates.Form
import dinf.backend.templates.Layout
import dinf.backend.templates.URLBlock
import dinf.domain.Dice
import dinf.domain.Dices
import dinf.domain.ID
import dinf.domain.SerialNumber
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
import kotlinx.html.FormMethod
import kotlinx.html.InputType
import kotlinx.html.a
import kotlinx.html.div
import kotlinx.html.form
import kotlinx.html.input
import kotlinx.html.ins
import kotlinx.html.p
import kotlinx.html.role
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
                            insert(view) {}
                            val location = call.locations.href(DiceLocation.ID(view.id))
                            a(href = location) {
                                role = "button"
                                +"Open"
                            }
                        }
                    }
                }
            }
        }
    }
}

fun Route.createForm(layout: Layout) {
    get<DiceLocation.New> { loc ->
        call.respondHtmlTemplate(layout) {
            content {
                val form = Form(loc.uri(call))
                insert(DiceForm(form)) {}
            }
        }
    }
}

fun Route.create(layout: Layout, editHashids: Hashids) {
    post<DiceLocation.New> { loc ->
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
                    val form = Form(loc.uri(call))
                    insert(DiceForm(form)) {
                        failed = true
                    }
                }
            }
        }
    }
}

fun Route.dice(layout: Layout, shareHashids: Hashids, baseURL: String) {
    get<DiceLocation.ID> { loc ->
        val dice = dices.diceOrNull(loc.id, shareHashids)
        if (dice == null) {
            call.respond(status = HttpStatusCode.NotFound, "")
        } else {
            call.respondHtmlTemplate(layout) {
                content {
                    val shareURL = loc.url(baseURL, call)
                    insert(URLBlock(shareURL)) {
                        text = "Share url: "
                    }
                    val diceID = ID.Simple(loc.id)
                    insert(DiceView(dice, diceID)) {}
                }
            }
        }
    }
}

fun Route.editForm(layout: Layout, shareHashids: Hashids, editHashids: Hashids, baseURL: String) {
    get<DiceLocation.Edit> { loc ->
        val dice = dices.diceOrNull(loc.id, editHashids)
        if (dice == null) {
            call.respond(status = HttpStatusCode.NotFound, "")
        } else {
            call.respondHtmlTemplate(layout) {
                content {
                    if (loc.updated) {
                        div {
                            ins {
                                +"Updated"
                            }
                        }
                    }

                    val shareURL = DiceLocation.ID(
                        HashID(dice, shareHashids)
                    ).url(baseURL, call)
                    insert(URLBlock(shareURL)) {
                        text = "Share url: "
                    }

                    val editURL = loc.url(baseURL, call)
                    insert(URLBlock(editURL)) {
                        text = "Edit url: "
                    }

                    val form = Form(loc.uri(call))
                    insert(DiceForm(form)) {
                        name = dice.name.nbString.toString()
                        edges = dice.edges.stringList.joinToString("\n")
                    }

                    val deleteURL = call.locations.href(DiceLocation.Delete(id = loc.id))
                    form(action = deleteURL, method = FormMethod.post) {
                        input(type = InputType.submit) {
                            value = "Delete"
                        }
                    }
                }
            }
        }
    }
}

fun Route.edit(layout: Layout, editHashids: Hashids) {
    post<DiceLocation.Edit> { loc ->
        val params = call.receiveParameters()
        val dice = dices.diceOrNull(loc.id, editHashids)
        if (dice == null) {
            call.respond(status = HttpStatusCode.NotFound, "")
        } else {
            val htmlDice = HTMLParamsDice.fromParametersOrNull(params)
            if (htmlDice != null) {
                dice.name.change(htmlDice.name.nbString)
                dice.edges.change(htmlDice.edges)
                val url = call.locations.href(loc.copy(updated = true))
                call.respondRedirect(url)
            } else {
                call.respondHtmlTemplate(layout) {
                    content {
                        val form = Form(loc.uri(call))
                        insert(DiceForm(form)) {
                            failed = true
                        }
                    }
                }
            }
        }
    }
}


fun Route.delete(layout: Layout, editHashids: Hashids) {
    post<DiceLocation.Delete> { loc ->
        val dice = dices.diceOrNull(loc.id, editHashids)
        if (dice == null) {
            call.respond(status = HttpStatusCode.NotFound, "")
        } else {
            dices.delete(dice)
            call.respondHtmlTemplate(layout) {
                content {
                    p { +"Dice deleted" }
                }
            }
        }
    }
}

fun Hashids.decodeOrNull(str: String): Long? {
    val array = decode(str)
    return if (array.isEmpty()) {
        null
    } else {
        array[0]
    }
}

suspend fun Dices.diceOrNull(id: String, hashids: Hashids): Dice? {
    return hashids.decodeOrNull(id)
        ?.let { SerialNumber.Simple(it) }
        ?.let { dice(it) }
}
