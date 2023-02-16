package dinf.routes

import dinf.domain.Count
import dinf.domain.DiceService
import dinf.domain.ID
import dinf.domain.Page
import dinf.html.components.DiceFeed
import dinf.html.pages.DiceCreatePage
import dinf.html.pages.DiceDeletedPage
import dinf.html.pages.DiceEditPage
import dinf.html.pages.DicePage
import dinf.html.pages.MainPage
import dinf.plugins.getUserSessionOrRedirectToNotFound
import dinf.plugins.respondPage
import dinf.types.toPLong
import io.ktor.server.application.*
import io.ktor.server.plugins.*
import io.ktor.server.request.*
import io.ktor.server.resources.*
import io.ktor.server.resources.post
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.index(diceService: DiceService, diceFeed: DiceFeed) {
    val page = 1
    val count = 10
    val searchAPI = application.href(HTMXResource.Search(page = page, count = count))
    val nextDicePageURL = application.href(HTMXResource.Dices(page = page + 1, count = count))
    get("/") {
        val diceList = diceService.find(Page(page), Count(count))
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
        getUserSessionOrRedirectToNotFound()
        call.respondPage(DiceCreatePage(url, resource))
    }
}

fun Route.create(diceService: DiceService) {
    post<DiceResource.New> { resource ->
        val session = getUserSessionOrRedirectToNotFound()
        val params = call.receiveParameters()
        val hashID = HTMLParamsDice.fromParametersOrNull(params)
            ?.let { diceService.saveDice(it.name, it.edges, ID(session.id.toPLong())) }
        val redirectURL = if (hashID != null) {
            application.href(DiceResource.Edit(diceID = hashID, firstTime = true))
        } else {
            application.href(resource.copy(isFailed = true))
        }
        call.respondRedirect(redirectURL)
    }
}

fun Route.dice(diceService: DiceService) {
    get<DiceResource.ByID> { resource ->
        val dice = diceService.findDiceByPublicID(resource.diceID)
        if (dice == null) {
            throw NotFoundException()
        } else {
            call.respondPage(DicePage(dice))
        }
    }
}

fun Route.editForm(baseURL: String, diceService: DiceService) {
    get<DiceResource.Edit> { resource ->
        val session = getUserSessionOrRedirectToNotFound()
        val dice = diceService.findDiceByPublicIdAndUserId(resource.diceID, ID(session.id.toPLong()))
        
        if (dice == null) {
            throw NotFoundException()
        } else {
            val editURL = application.href(DiceResource.Edit(diceID = resource.diceID))
            val deleteURL = application.href(DiceResource.Delete(diceID = resource.diceID))
            call.respondPage(DiceEditPage(dice, "$baseURL$editURL", deleteURL, resource))
        }
    }
}

fun Route.edit(diceService: DiceService) {
    post<DiceResource.Edit> { resource ->
        val session = getUserSessionOrRedirectToNotFound()
        val dice = diceService.findDiceByPublicIdAndUserId(resource.diceID, ID(session.id.toPLong()))

        if (dice == null) {
            throw NotFoundException()
        } else {
            val params = call.receiveParameters()
            val htmlDice = HTMLParamsDice.fromParametersOrNull(params)
            val redirectURL = if (htmlDice != null) {
                dice.change(htmlDice.name, htmlDice.edges)
                application.href(resource)
            } else {
                application.href(resource.copy(isFailed = true))
            }
            call.respondRedirect(redirectURL)
        }
    }
}


fun Route.delete(diceService: DiceService) {
    post<DiceResource.Delete> { resource ->
        val session = getUserSessionOrRedirectToNotFound()
        diceService.deleteByPublicIdAndUserId(resource.diceID, ID(session.id.toPLong()))
        call.respondPage(DiceDeletedPage())
    }
}
