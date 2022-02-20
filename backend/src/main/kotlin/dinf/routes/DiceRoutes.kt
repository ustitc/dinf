package dinf.routes

import dinf.adapters.HashID
import dinf.domain.Dice
import dinf.domain.DiceDelete
import dinf.domain.DiceMetrics
import dinf.domain.DiceSave
import dinf.domain.Dices
import dinf.domain.SerialNumber
import dinf.html.components.DiceFeed
import dinf.html.templates.SearchBar
import dinf.html.templates.DiceForm
import dinf.html.templates.Form
import dinf.html.templates.Layout
import dinf.html.templates.RollButton
import dinf.html.templates.URLBlock
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.html.*
import io.ktor.locations.*
import io.ktor.locations.post
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import kotlinx.coroutines.flow.toList
import kotlinx.html.FormMethod
import kotlinx.html.InputType
import kotlinx.html.form
import kotlinx.html.h2
import kotlinx.html.input
import kotlinx.html.p
import org.hashids.Hashids

fun Route.index(layout: Layout, dices: Dices, diceFeed: DiceFeed) {
    val searchAPI = application.locations.href(HTMXLocations.Search())

    get("/") {
        val diceList = dices.flow().toList()

        call.respondHtmlTemplate(layout) {
            content {
                insert(SearchBar(searchAPI)) {
                    initialContent {
                        diceFeed.component(this, diceList)
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

fun Route.create(layout: Layout, editHashids: Hashids, diceSave: DiceSave) {
    post<DiceLocation.New> { loc ->
        val params = call.receiveParameters()
        val dice = HTMLParamsDice.fromParametersOrNull(params)
            ?.let { diceSave.create(it) }
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

fun Route.dice(layout: Layout, shareHashids: Hashids, baseURL: String, dices: Dices, diceMetrics: DiceMetrics) {
    get<DiceLocation.ID> { loc ->
        val dice = dices.diceOrNull(loc.id, shareHashids)
        if (dice == null) {
            throw NotFoundException()
        } else {
            diceMetrics.forDice(dice).increment()
            val shareURL = loc.url(baseURL, call)

            call.respondHtmlTemplate(layout) {
                content {
                    h2 { +dice.name.nbString.toString() }

                    insert(URLBlock(shareURL)) {
                        text = "Share url: "
                    }

                    insert(RollButton(dice)) {
                        resultTagID = "result"
                    }
                }
            }
        }
    }
}

fun Route.editForm(layout: Layout, shareHashids: Hashids, editHashids: Hashids, baseURL: String, dices: Dices) {
    get<DiceLocation.Edit> { loc ->
        val dice = dices.diceOrNull(loc.id, editHashids)
        if (dice == null) {
            throw NotFoundException()
        } else {
            val diceID = HashID(dice, shareHashids)
            val shareURL = DiceLocation.ID(diceID).url(baseURL, call)
            val editURL = loc.url(baseURL, call)
            val deleteURL = call.locations.href(DiceLocation.Delete(id = loc.id))
            val form = Form(loc.uri(call))

            call.respondHtmlTemplate(layout) {
                content {
                    insert(URLBlock(shareURL)) {
                        text = "Share url: "
                    }
                    insert(URLBlock(editURL)) {
                        text = "Edit url: "
                    }

                    insert(RollButton(dice)) {
                        resultTagID = "result"
                    }

                    insert(DiceForm(form)) {
                        name = dice.name.nbString.toString()
                        edges = dice.edges.stringList.joinToString("\n")
                    }

                    form(action = deleteURL, method = FormMethod.post) {
                        input(type = InputType.submit, classes = "delete") {
                            value = "Delete"
                        }
                    }
                }
            }
        }
    }
}

fun Route.edit(layout: Layout, editHashids: Hashids, dices: Dices) {
    post<DiceLocation.Edit> { loc ->
        val params = call.receiveParameters()
        val dice = dices.diceOrNull(loc.id, editHashids)
        if (dice == null) {
            throw NotFoundException()
        } else {
            val htmlDice = HTMLParamsDice.fromParametersOrNull(params)
            if (htmlDice != null) {
                dice.name.change(htmlDice.name.nbString)
                dice.edges.change(htmlDice.edges)
                val url = call.locations.href(loc)
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


fun Route.delete(layout: Layout, editHashids: Hashids, dices: Dices, diceDelete: DiceDelete) {
    post<DiceLocation.Delete> { loc ->
        val dice = dices.diceOrNull(loc.id, editHashids)
        if (dice == null) {
            throw NotFoundException()
        } else {
            diceDelete.delete(dice)
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
        ?.let { oneOrNull(it) }
}