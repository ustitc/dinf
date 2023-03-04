package dinf.app.plugins

import dinf.app.auth.UserSession
import dinf.app.config.AppConfig
import dinf.app.html.pages.NotFoundPage
import dinf.app.html.pages.Page
import dinf.app.html.templates.Layout
import dinf.app.routes.DiceResource
import dinf.app.routes.LoginResource
import dinf.app.routes.LogoutResource
import dinf.app.routes.RegisterResource
import dinf.app.routes.diceCreateRoutes
import dinf.app.routes.diceDeleteRoutes
import dinf.app.routes.diceEditRoutes
import dinf.app.routes.dicePage
import dinf.app.routes.emailPasswordLoginRoutes
import dinf.app.routes.htmxDiceList
import dinf.app.routes.htmxDiceSearch
import dinf.app.routes.loginPage
import dinf.app.routes.logout
import dinf.app.routes.mainPage
import dinf.app.routes.oAuthGoogleRoute
import dinf.app.routes.registrationRoutes
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
        diceEditRoutes()
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
