package dinf.routes

import dinf.adapters.HashID
import dinf.domain.Count
import dinf.domain.Dice
import dinf.domain.DiceDelete
import dinf.domain.DiceGet
import dinf.domain.DiceMetrics
import dinf.domain.DiceSave
import dinf.domain.Dices
import dinf.domain.Metric
import dinf.domain.Page
import dinf.domain.SerialNumber
import dinf.html.components.DiceFeed
import dinf.html.components.picoInlineButton
import dinf.html.templates.SearchBar
import dinf.html.templates.Layout
import dinf.html.templates.RollBlock
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.html.*
import io.ktor.locations.*
import io.ktor.locations.post
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import kotlinx.html.FormMethod
import kotlinx.html.InputType
import kotlinx.html.a
import kotlinx.html.article
import kotlinx.html.dialog
import kotlinx.html.div
import kotlinx.html.form
import kotlinx.html.h2
import kotlinx.html.h3
import kotlinx.html.hGroup
import kotlinx.html.input
import kotlinx.html.p
import org.hashids.Hashids

private val componentDeps = ComponentDeps()

fun Route.index(layout: Layout, diceGet: DiceGet, diceFeed: DiceFeed) {
    val page = 1
    val count = 10
    val searchAPI = application.locations.href(HTMXLocations.Search(page = page, count = count))
    get("/") {
        val diceList = diceGet.invoke(Page(page), Count(count))
        val nextDicePageURL = application.locations.href(HTMXLocations.Dices(page = page + 1, count = count))
        call.respondHtmlTemplate(layout) {
            content {
                insert(SearchBar(searchAPI)) {
                    initialContent {
                        diceFeed.component(this, diceList, nextDicePageURL)
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
                val form = componentDeps.diceForm(loc.uri(call))
                insert(form) {}
            }
        }
    }
}

fun Route.create(layout: Layout, editHashids: Hashids, diceSave: DiceSave) {
    post<DiceLocation.New> { loc ->
        val params = call.receiveParameters()
        val dice = HTMLParamsDice.fromParametersOrNull(params)
            ?.let { Dice.New(it.name, it.edges) }
            ?.let { diceSave.invoke(it) }
        if (dice != null) {
            val id = HashID(dice, editHashids)
            val url = call.locations.href(DiceLocation.Edit(id))
            call.respondRedirect(url)
        } else {
            call.respondHtmlTemplate(layout) {
                content {
                    val form = componentDeps.diceForm(loc.uri(call))
                    insert(form) {
                        failed = true
                    }
                }
            }
        }
    }
}

fun Route.dice(layout: Layout, shareHashids: Hashids, dices: Dices, diceMetrics: DiceMetrics) {
    get<DiceLocation.ID> { loc ->
        val dice = dices.diceOrNull(loc.id, shareHashids)
        if (dice == null) {
            throw NotFoundException()
        } else {
            val metric = diceMetrics.forDice(dice)
            if (metric == null) {
                diceMetrics.create(dice, Metric.Simple(1))
            } else {
                metric.addClick()
            }

            call.respondHtmlTemplate(layout) {
                content {
                    h2 { +dice.name.print() }

                    insert(RollBlock(dice)) {
                    }
                }
            }
        }
    }
}

fun Route.editForm(layout: Layout, editHashids: Hashids, baseURL: String, dices: Dices) {
    get<DiceLocation.Edit> { loc ->
        val dice = dices.diceOrNull(loc.id, editHashids)
        if (dice == null) {
            throw NotFoundException()
        } else {
            val editURL = loc.url(baseURL, call)
            val deleteURL = call.locations.href(DiceLocation.Delete(id = loc.id))
            val form = componentDeps.diceForm(loc.uri(call))

            call.respondHtmlTemplate(layout) {
                content {
                    hGroup {
                        h2 { +dice.name.print() }
                        h3 {
                            text("Save this link to edit your dice later: ")
                            a(href = editURL) { +editURL }
                        }
                    }

                    dialog {
                        attributes["open"] = "false"
                        article {
                            p {
                                +"Save this link to edit your dice later!"
                            }
                            div("grid") {
                                div("container") {
                                    input {
                                        value = editURL
                                        readonly = true
                                    }
                                }
                                div {
                                    picoInlineButton("contrast") {
                                        +"Copy"
                                    }
                                }
                            }
                        }
                    }

                    insert(RollBlock(dice)) {
                    }

                    insert(form) {
                        name = dice.name.print()
                        edges = dice.edges.stringList
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
                dice.change(htmlDice.name)
                dice.edges.change(htmlDice.edges)
                val url = call.locations.href(loc)
                call.respondRedirect(url)
            } else {
                call.respondHtmlTemplate(layout) {
                    content {
                        val form = componentDeps.diceForm(loc.uri(call))
                        insert(form) {
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
            diceDelete.invoke(dice)
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
