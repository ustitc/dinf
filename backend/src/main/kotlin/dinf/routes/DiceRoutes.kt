package dinf.routes

import dinf.domain.Count
import dinf.domain.Dice
import dinf.domain.DiceDelete
import dinf.domain.DiceGet
import dinf.domain.DiceMetricRepository
import dinf.domain.DiceFactory
import dinf.domain.DiceRepository
import dinf.domain.HashIDs
import dinf.domain.Metric
import dinf.domain.Page
import dinf.html.components.DiceFeed
import dinf.html.templates.Layout
import dinf.html.templates.RollBlock
import dinf.html.templates.SearchBar
import io.ktor.resources.*
import io.ktor.resources.serialization.*
import io.ktor.server.application.*
import io.ktor.server.html.*
import io.ktor.server.plugins.*
import io.ktor.server.request.*
import io.ktor.server.resources.*
import io.ktor.server.resources.post
import io.ktor.server.resources.get
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.html.h2
import kotlinx.html.p

private val componentDeps = ComponentDeps()

fun Route.index(layout: Layout, diceGet: DiceGet, diceFeed: DiceFeed) {
    val page = 1
    val count = 10
    val searchAPI = href(ResourcesFormat(), HTMXResource.Search(page = page, count = count))
    get("/") {
        val diceList = diceGet.invoke(Page(page), Count(count))
        val nextDicePageURL = href(ResourcesFormat(), HTMXResource.Dices(page = page + 1, count = count))
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
    val url = application.href(DiceResource.New())
    get<DiceResource.New> { loc ->
        call.respondHtmlTemplate(layout) {
            content {
                val form = componentDeps.diceForm(url)
                insert(form) {}
            }
        }
    }
}

fun Route.create(layout: Layout, editHashids: HashIDs, diceFactory: DiceFactory) {
    val url = application.href(DiceResource.New())
    post<DiceResource.New> { loc ->
        val params = call.receiveParameters()
        val dice = HTMLParamsDice.fromParametersOrNull(params)
            ?.let { Dice.New(it.name, it.edges) }
            ?.let { diceFactory.create(it) }
        if (dice != null) {
            val id = editHashids.fromID(dice.id)
            val diceURL = href(ResourcesFormat(), DiceResource.Edit(hashID = id, firstTime = true))
            call.respondRedirect(diceURL)
        } else {
            call.respondHtmlTemplate(layout) {
                content {
                    val form = componentDeps.diceForm(url)
                    insert(form) {
                        failed = true
                    }
                }
            }
        }
    }
}

fun Route.dice(layout: Layout, shareHashids: HashIDs, diceRepository: DiceRepository, diceMetricRepository: DiceMetricRepository) {
    get<DiceResource.ByHashID> { loc ->
        val dice = shareHashids.fromStringOrNull(loc.hashID)?.let { diceRepository.oneOrNull(it) }
        if (dice == null) {
            throw NotFoundException()
        } else {
            val metric = diceMetricRepository.forDice(dice)
            if (metric == null) {
                diceMetricRepository.create(dice, Metric.Simple(1))
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

fun Route.editForm(layout: Layout, editHashids: HashIDs, baseURL: String, diceRepository: DiceRepository) {
    get<DiceResource.Edit> { loc ->
        val dice = editHashids.fromStringOrNull(loc.hashID)?.let { diceRepository.oneOrNull(it) }
        if (dice == null) {
            throw NotFoundException()
        } else {
            val editURL =application.href(DiceResource.Edit(hashID = loc.hashID))
            val deleteURL = application.href(DiceResource.Delete(hashID = loc.hashID))
            val isOpenDialog = loc.firstTime ?: false
            call.respondHtmlTemplate(layout) {
                insert(componentDeps.diceEditPage(dice, "$baseURL$editURL", deleteURL)) {
                    dialogOpen = isOpenDialog
                }
            }
        }
    }
}

fun Route.edit(layout: Layout, editHashids: HashIDs, diceRepository: DiceRepository) {
    post<DiceResource.Edit> { loc ->
        val url = application.href(loc)
        val params = call.receiveParameters()
        val dice = editHashids.fromStringOrNull(loc.hashID)?.let { diceRepository.oneOrNull(it) }
        if (dice == null) {
            throw NotFoundException()
        } else {
            val htmlDice = HTMLParamsDice.fromParametersOrNull(params)
            if (htmlDice != null) {
                dice.change(htmlDice.name, htmlDice.edges)
                call.respondRedirect(url)
            } else {
                call.respondHtmlTemplate(layout) {
                    content {
                        val form = componentDeps.diceForm(url)
                        insert(form) {
                            failed = true
                        }
                    }
                }
            }
        }
    }
}


fun Route.delete(layout: Layout, editHashids: HashIDs, diceRepository: DiceRepository, diceDelete: DiceDelete) {
    post<DiceResource.Delete> { loc ->
        val dice = editHashids.fromStringOrNull(loc.hashID)?.let { diceRepository.oneOrNull(it) }
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
