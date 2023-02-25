package dinf.app.routes

import dinf.app.deps
import dinf.domain.Count
import dinf.domain.ID
import dinf.domain.Page
import dinf.app.html.pages.DiceCreatePage
import dinf.app.html.pages.DiceDeletedPage
import dinf.app.html.pages.DiceEditPage
import dinf.app.html.pages.DicePage
import dinf.app.html.pages.MainPage
import dinf.app.plugins.getUserSessionOrRedirectToNotFound
import dinf.app.plugins.respondPage
import dinf.types.toPLong
import io.ktor.server.application.*
import io.ktor.server.plugins.*
import io.ktor.server.request.*
import io.ktor.server.resources.*
import io.ktor.server.resources.post
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.mainPage() {
    val page = 1
    val count = 10
    val searchAPI = application.href(HTMXResource.Search(page = page, count = count))
    val nextDicePageURL = application.href(HTMXResource.Dices(page = page + 1, count = count))
    get("/") {
        val diceFeed = deps.diceFeedComponentFactory(call)
        val diceList = deps.diceService().find(Page(page), Count(count))
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

fun Route.diceCreateRoutes() {
    val url = application.href(DiceResource.New())

    get<DiceResource.New> { resource ->
        getUserSessionOrRedirectToNotFound()
        call.respondPage(DiceCreatePage(url, resource))
    }

    post<DiceResource.New> { resource ->
        val session = getUserSessionOrRedirectToNotFound()
        val params = call.receiveParameters()
        val hashID = HTMLParamsDice.fromParametersOrNull(params)
            ?.let { deps.diceService().saveDice(it.name, it.edges, ID(session.id.toPLong())) }
        val redirectURL = if (hashID != null) {
            application.href(DiceResource.Edit(diceID = hashID, firstTime = true))
        } else {
            application.href(resource.copy(isFailed = true))
        }
        call.respondRedirect(redirectURL)
    }
}

fun Route.dicePage() {
    get<DiceResource.ByID> { resource ->
        val dice = deps.diceService().findDiceByPublicID(resource.diceID)
        if (dice == null) {
            throw NotFoundException()
        } else {
            call.respondPage(DicePage(dice))
        }
    }
}

fun Route.diceEditRoutes(baseURL: String) {
    get<DiceResource.Edit> { resource ->
        val session = getUserSessionOrRedirectToNotFound()
        val dice = deps.diceService().findDiceByPublicIdAndUserId(resource.diceID, ID(session.id.toPLong()))
        
        if (dice == null) {
            throw NotFoundException()
        } else {
            val editURL = application.href(DiceResource.Edit(diceID = resource.diceID))
            val deleteURL = application.href(DiceResource.Delete(diceID = resource.diceID))
            call.respondPage(DiceEditPage(dice, "$baseURL$editURL", deleteURL, resource))
        }
    }

    post<DiceResource.Edit> { resource ->
        val session = getUserSessionOrRedirectToNotFound()
        val dice = deps.diceService().findDiceByPublicIdAndUserId(resource.diceID, ID(session.id.toPLong()))

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

fun Route.diceDeleteRoutes() {
    post<DiceResource.Delete> { resource ->
        val session = getUserSessionOrRedirectToNotFound()
        deps.diceService().deleteByPublicIdAndUserId(resource.diceID, ID(session.id.toPLong()))
        call.respondPage(DiceDeletedPage())
    }
}
