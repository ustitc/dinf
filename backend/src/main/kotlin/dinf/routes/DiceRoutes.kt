package dinf.routes

import dinf.domain.Count
import dinf.domain.DiceDelete
import dinf.domain.DiceGet
import dinf.domain.DiceMetricRepository
import dinf.domain.DiceRepository
import dinf.domain.DiceService
import dinf.domain.HashIDFactory
import dinf.domain.Metric
import dinf.domain.Page
import dinf.html.components.DiceFeed
import dinf.html.pages.DiceCreatePage
import dinf.html.pages.DiceDeletedPage
import dinf.html.pages.DiceEditPage
import dinf.html.pages.DicePage
import dinf.html.pages.MainPage
import dinf.plugins.respondPage
import io.ktor.resources.*
import io.ktor.resources.serialization.*
import io.ktor.server.application.*
import io.ktor.server.plugins.*
import io.ktor.server.request.*
import io.ktor.server.resources.*
import io.ktor.server.resources.post
import io.ktor.server.resources.get
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.index(diceGet: DiceGet, diceFeed: DiceFeed) {
    val page = 1
    val count = 10
    val searchAPI = href(ResourcesFormat(), HTMXResource.Search(page = page, count = count))
    get("/") {
        val diceList = diceGet.invoke(Page(page), Count(count))
        val nextDicePageURL = href(ResourcesFormat(), HTMXResource.Dices(page = page + 1, count = count))
        call.respondPage(
            MainPage(
                searchURL = searchAPI,
                nextDicePageURL = nextDicePageURL,
                diceList = diceList,
                diceFeed = diceFeed
            )
        )
    }
}

fun Route.createForm() {
    val url = application.href(DiceResource.New())
    get<DiceResource.New> { resource ->
        call.respondPage(DiceCreatePage(url, resource))
    }
}

fun Route.create(diceService: DiceService) {
    post<DiceResource.New> { resource ->
        val params = call.receiveParameters()
        val hashID = HTMLParamsDice.fromParametersOrNull(params)
            ?.let { diceService.saveDice(it.name, it.edges) }
        val redirectURL = if (hashID != null) {
            application.href(DiceResource.Edit(hashID = hashID, firstTime = true))
        } else {
            application.href(resource.copy(isFailed = true))
        }
        call.respondRedirect(redirectURL)
    }
}

fun Route.dice(
    shareHashids: HashIDFactory,
    diceRepository: DiceRepository,
    diceMetricRepository: DiceMetricRepository
) {
    get<DiceResource.ByHashID> { loc ->
        val dice = shareHashids.fromStringOrNull(loc.hashID)?.let { diceRepository.oneOrNull(it) }
        if (dice == null) {
            throw NotFoundException()
        } else {
            val metric = diceMetricRepository.forID(dice.id)
            if (metric == null) {
                diceMetricRepository.create(dice.id, Metric.Simple(1))
            } else {
                metric.addClick()
            }

            call.respondPage(DicePage(dice))
        }
    }
}

fun Route.editForm(editHashids: HashIDFactory, baseURL: String, diceRepository: DiceRepository) {
    get<DiceResource.Edit> { resource ->
        val dice = editHashids.fromStringOrNull(resource.hashID)?.let { diceRepository.oneOrNull(it) }
        if (dice == null) {
            throw NotFoundException()
        } else {
            val editURL = application.href(DiceResource.Edit(hashID = resource.hashID))
            val deleteURL = application.href(DiceResource.Delete(hashID = resource.hashID))
            call.respondPage(DiceEditPage(dice, "$baseURL$editURL", deleteURL, resource))
        }
    }
}

fun Route.edit(editHashids: HashIDFactory, diceRepository: DiceRepository) {
    post<DiceResource.Edit> { loc ->
        val params = call.receiveParameters()
        val dice = editHashids.fromStringOrNull(loc.hashID)?.let { diceRepository.oneOrNull(it) }
        if (dice == null) {
            throw NotFoundException()
        } else {
            val htmlDice = HTMLParamsDice.fromParametersOrNull(params)
            val redirectURL = if (htmlDice != null) {
                dice.change(htmlDice.name, htmlDice.edges)
                application.href(loc)
            } else {
                application.href(loc.copy(isFailed = true))
            }
            call.respondRedirect(redirectURL)
        }
    }
}


fun Route.delete(editHashids: HashIDFactory, diceRepository: DiceRepository, diceDelete: DiceDelete) {
    post<DiceResource.Delete> { loc ->
        val dice = editHashids.fromStringOrNull(loc.hashID)?.let { diceRepository.oneOrNull(it) }
        if (dice == null) {
            throw NotFoundException()
        } else {
            diceDelete.invoke(dice)
            call.respondPage(DiceDeletedPage())
        }
    }
}
