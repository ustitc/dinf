package dinf.app.routes

import dinf.app.auth.UserSession
import dinf.app.deps
import dinf.app.html.pages.DiceCreatePage
import dinf.app.html.pages.DiceDeletedPage
import dinf.app.html.pages.DiceEditPage
import dinf.app.html.pages.DicePage
import dinf.app.html.pages.MainPage
import dinf.app.plugins.getUserSessionOrRedirectToNotFound
import dinf.app.plugins.respondPage
import dinf.domain.Count
import dinf.domain.Page
import io.ktor.server.application.*
import io.ktor.server.plugins.*
import io.ktor.server.request.*
import io.ktor.server.resources.*
import io.ktor.server.resources.post
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*

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

    post<DiceResource.New> {
        val session = getUserSessionOrRedirectToNotFound()
        val params = call.receiveParameters()
        val dice = deps.diceViewService().createDice(session, params)
        val redirect = application.href(DiceResource.ByID(diceID = dice.id))
        call.respondRedirect(redirect)
    }
}

fun Route.dicePage() {
    get<DiceResource.ByID> { resource ->
        val session = call.sessions.get<UserSession>()
        val dice = deps.diceViewService().findDice(resource.diceID, session)
        when {
            dice == null -> throw NotFoundException()
            session != null && dice.belongsTo(session) -> call.respondPage(DiceEditPage(dice = dice))
            else -> call.respondPage(DicePage(dice))
        }
    }
}

fun Route.diceEditRoutes() {
    post<DiceResource.Edit> { resource ->
        val session = getUserSessionOrRedirectToNotFound()
        deps.diceViewService().updateDice(resource.diceID, session, call.receiveParameters())
        val redirect = application.href(DiceResource.ByID(diceID = resource.diceID))
        call.respondRedirect(redirect)
    }
}

fun Route.diceDeleteRoutes() {
    post<DiceResource.Delete> { resource ->
        val session = getUserSessionOrRedirectToNotFound()
        deps.diceViewService().deleteDice(resource.diceID, session)
        call.respondPage(DiceDeletedPage())
    }
}
