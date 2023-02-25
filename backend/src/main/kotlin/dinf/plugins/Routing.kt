package dinf.plugins

import dinf.auth.UserSession
import dinf.config.AppConfig
import dinf.html.pages.NotFoundPage
import dinf.html.pages.Page
import dinf.html.templates.Layout
import dinf.routes.DiceResource
import dinf.routes.LoginResource
import dinf.routes.LogoutResource
import dinf.routes.RegisterResource
import dinf.routes.diceCreateRoutes
import dinf.routes.diceDeleteRoutes
import dinf.routes.dicePage
import dinf.routes.diceEditRoutes
import dinf.routes.emailPasswordLoginRoutes
import dinf.routes.htmxDiceList
import dinf.routes.mainPage
import dinf.routes.loginPage
import dinf.routes.logout
import dinf.routes.oAuthGoogleRoute
import dinf.routes.registrationRoutes
import dinf.routes.htmxDiceSearch
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.html.*
import io.ktor.server.http.content.*
import io.ktor.server.plugins.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.resources.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*
import io.ktor.server.webjars.*
import io.ktor.util.pipeline.*

private lateinit var cfg: AppConfig

fun ApplicationCall.isLoginedUser(): Boolean {
    return sessions.get<UserSession>() != null
}

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
            showUserButtons = cfg.toggles.showUserButtons,
            registartionEnabled = cfg.login.password.enabled
        )
    ) {
        insert(page) {}
    }
}

fun PipelineContext<*, ApplicationCall>.getUserSessionOrRedirectToNotFound(): UserSession {
    val session = call.sessions.get<UserSession>()
    return session ?: throw NotFoundException()
}

fun Application.configureRouting(config: AppConfig) {
    cfg = config
    val baseURL = config.server.baseURL

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
            loginPage(cfg.login)
            logout()

            if (cfg.login.password.enabled) {
                registrationRoutes()
                emailPasswordLoginRoutes()
            }
        }
        if (cfg.login.oauth.google.enabled) {
            oAuthGoogleRoute()
        }
        mainPage()
        dicePage()
        diceCreateRoutes()
        diceEditRoutes(baseURL = baseURL)
        diceDeleteRoutes()
        htmxDiceSearch()
        htmxDiceList()

        static("assets") {
            resources("js")
            resources("css")
            resources("img")
        }
    }
}
