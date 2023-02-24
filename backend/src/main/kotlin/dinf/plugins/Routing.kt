package dinf.plugins

import dinf.AppDeps
import dinf.auth.UserSession
import dinf.html.components.DiceCard
import dinf.html.components.DiceFeed
import dinf.config.Configuration
import dinf.html.pages.NotFoundPage
import dinf.html.pages.Page
import dinf.routes.DiceResource
import dinf.routes.create
import dinf.routes.createForm
import dinf.routes.dice
import dinf.routes.delete
import dinf.routes.index
import dinf.routes.edit
import dinf.routes.editForm
import dinf.routes.search
import dinf.html.templates.Layout
import dinf.routes.LoginResource
import dinf.routes.LogoutResource
import dinf.routes.RegisterResource
import dinf.routes.htmxDices
import dinf.routes.login
import dinf.routes.loginPage
import dinf.routes.logout
import dinf.routes.oAuthGoogle
import dinf.routes.register
import dinf.routes.registrationPage
import io.ktor.server.application.*
import io.ktor.server.html.*
import io.ktor.http.*
import io.ktor.server.http.content.*
import io.ktor.server.plugins.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.resources.href
import io.ktor.server.routing.*
import io.ktor.server.sessions.*
import io.ktor.server.webjars.*
import io.ktor.util.pipeline.*

private lateinit var cfg: Configuration

suspend fun ApplicationCall.respondPage(page: Page) {
    val session = sessions.get<UserSession>()
    respondHtmlTemplate(
        Layout(
            newDiceURL = application.href(DiceResource.New()),
            htmxConfiguration = cfg.htmx,
            loginURL = application.href(LoginResource()),
            logoutURL = application.href(LogoutResource),
            registerURL = application.href(RegisterResource()),
            userSession = session,
            showUserButtons = cfg.toggles.showUserButtons
        )
    ) {
        insert(page) {}
    }
}

fun PipelineContext<*, ApplicationCall>.getUserSessionOrRedirectToNotFound(): UserSession {
    val session = call.sessions.get<UserSession>()
    return session ?: throw NotFoundException()
}

fun Application.configureRouting(
    config: Configuration,
    deps: AppDeps
) {
    cfg = config
    val baseURL = config.server.baseURL

    val newDiceURL = href(DiceResource.New())

    val diceCard = DiceCard(publicIDFactory = deps.publicIDFactory())
    val diceFeed = DiceFeed(newDiceURL = newDiceURL, diceCard = diceCard)

    install(StatusPages) {
        status(HttpStatusCode.NotFound) { call, _ ->
            call.respondPage(NotFoundPage())
        }
    }

    install(Webjars) {
        path = "assets"
    }

    routing {
        if (cfg.toggles.showUserButtons) {
            loginPage()
            registrationPage()
            login()
            register(deps.emailPasswordService())
            logout()
        }
        if (cfg.login.oauth.google.enabled) {
            oAuthGoogle(deps.oAuthService())
        }
        index(
            diceService = deps.diceService(),
            diceFeed = diceFeed
        )
        create(
            diceService = deps.diceService()
        )
        createForm()
        dice(
            diceService = deps.diceService()
        )
        edit(
            diceService = deps.diceService()
        )
        editForm(
            baseURL = baseURL,
            diceService = deps.diceService()
        )
        delete(
            diceService = deps.diceService()
        )
        search(
            diceService = deps.diceService(),
            diceFeed = diceFeed
        )
        htmxDices(
            diceService = deps.diceService(),
            diceFeed = diceFeed
        )

        static("assets") {
            resources("js")
            resources("css")
            resources("img")
        }
    }
}
