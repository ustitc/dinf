package dinf.routes

import dinf.domain.Count
import dinf.domain.Dice
import dinf.domain.DiceDelete
import dinf.domain.DiceGet
import dinf.domain.DiceMetrics
import dinf.domain.DiceSave
import dinf.domain.Dices
import dinf.domain.HashIDs
import dinf.domain.Metric
import dinf.domain.Page
import dinf.html.components.DiceFeed
import dinf.html.templates.Layout
import dinf.html.templates.RollBlock
import dinf.html.templates.SearchBar
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.html.*
import io.ktor.locations.*
import io.ktor.locations.post
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import kotlinx.html.h2
import kotlinx.html.p

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

fun Route.create(layout: Layout, editHashids: HashIDs, diceSave: DiceSave) {
    post<DiceLocation.New> { loc ->
        val params = call.receiveParameters()
        val dice = HTMLParamsDice.fromParametersOrNull(params)
            ?.let { Dice.New(it.name, it.edges) }
            ?.let { diceSave.invoke(it) }
        if (dice != null) {
            val id = editHashids.fromID(dice.id)
            val url = call.locations.href(DiceLocation.Edit(hashID = id, firstTime = true))
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

fun Route.dice(layout: Layout, shareHashids: HashIDs, dices: Dices, diceMetrics: DiceMetrics) {
    get<DiceLocation.ByHashID> { loc ->
        val dice = shareHashids.fromStringOrNull(loc.hashID)?.let { dices.oneOrNull(it) }
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

fun Route.editForm(layout: Layout, editHashids: HashIDs, baseURL: String, dices: Dices) {
    get<DiceLocation.Edit> { loc ->
        val dice = editHashids.fromStringOrNull(loc.hashID)?.let { dices.oneOrNull(it) }
        if (dice == null) {
            throw NotFoundException()
        } else {
            val editURL = DiceLocation.Edit(hashID = loc.hashID).url(baseURL, call)
            val deleteURL = call.locations.href(DiceLocation.Delete(id = loc.hashID))
            val isOpenDialog = loc.firstTime ?: false
            call.respondHtmlTemplate(layout) {
                insert(componentDeps.diceEditPage(dice, editURL, deleteURL)) {
                    dialogOpen = isOpenDialog
                }
            }
        }
    }
}

fun Route.edit(layout: Layout, editHashids: HashIDs, dices: Dices) {
    post<DiceLocation.Edit> { loc ->
        val params = call.receiveParameters()
        val dice = editHashids.fromStringOrNull(loc.hashID)?.let { dices.oneOrNull(it) }
        if (dice == null) {
            throw NotFoundException()
        } else {
            val htmlDice = HTMLParamsDice.fromParametersOrNull(params)
            if (htmlDice != null) {
                dice.change(htmlDice.name, htmlDice.edges)
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


fun Route.delete(layout: Layout, editHashids: HashIDs, dices: Dices, diceDelete: DiceDelete) {
    post<DiceLocation.Delete> { loc ->
        val dice = editHashids.fromStringOrNull(loc.id)?.let { dices.oneOrNull(it) }
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
