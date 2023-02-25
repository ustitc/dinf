package dinf.app.routes

import dinf.app.auth.Credential
import dinf.app.auth.EmailPasswordService
import dinf.app.auth.OAuthService
import dinf.app.auth.UserPrincipal
import dinf.app.auth.UserSession
import dinf.app.config.LoginConfig
import dinf.app.deps
import dinf.app.html.pages.LoginPage
import dinf.app.html.pages.RegistrationPage
import dinf.app.plugins.FORM_LOGIN_CONFIGURATION_NAME
import dinf.app.plugins.FORM_LOGIN_EMAIL_FIELD
import dinf.app.plugins.FORM_LOGIN_PASSWORD_FIELD
import dinf.app.plugins.OAUTH_GOOGLE_CONFIGURATION_NAME
import dinf.app.plugins.respondPage
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.resources.*
import io.ktor.server.resources.post
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*

fun Route.loginPage(loginConfig: LoginConfig) {
    val googleLoginUrl = application.href(OAuthResource.Google())
    get<LoginResource> {
        call.respondPage(
            LoginPage(
                resource = it,
                googleLoginUrl = googleLoginUrl,
                emailPasswordEnabled = loginConfig.password.enabled,
                googleLoginEnabled = loginConfig.oauth.google.enabled
            )
        )
    }
}

fun Route.registrationRoutes() {
    get<RegisterResource> {
        call.respondPage(RegistrationPage(it))
    }

    post<RegisterResource> {
        val params = call.receiveParameters()
        val email = params[FORM_LOGIN_EMAIL_FIELD]
        val password = params[FORM_LOGIN_PASSWORD_FIELD]
        requireNotNull(email)
        requireNotNull(password)

        val auth = Credential.EmailPassword(email, password)
        when (val result = deps.emailPasswordService().register(auth)) {
            is EmailPasswordService.Registration.Created -> call.setSessionAndRedirect(result.userPrincipal.session)
            is EmailPasswordService.Registration.Exists -> {
                val redirect = application.href(RegisterResource(userExists = true))
                call.respondRedirect(redirect)
            }
        }
    }
}

fun Route.emailPasswordLoginRoutes() {
    authenticate(FORM_LOGIN_CONFIGURATION_NAME) {
        post<LoginResource> {
            val principal = call.principal<UserPrincipal>()!!
            call.setSessionAndRedirect(principal.session)
        }
    }
}

fun Route.logout() {
    get<LogoutResource> {
        call.sessions.clear<UserSession>()
        call.respondRedirect("/")
    }
}

fun Route.oAuthGoogleRoute() {
    authenticate(OAUTH_GOOGLE_CONFIGURATION_NAME) {
        get<OAuthResource.Google> {
            // Redirects to 'authorizeUrl' automatically
        }

        get<OAuthResource.Google.Callback> {
            val principal: OAuthAccessTokenResponse.OAuth2? = call.principal()
            requireNotNull(principal) { "Callback must contain token" }

            val auth = Credential.Google(principal.accessToken)
            when (val login = deps.oAuthService().login(auth)) {
                is OAuthService.Login.Ok -> {
                    call.sessions.set(login.userPrincipal.session)
                    call.respondRedirect("/")
                }
                is OAuthService.Login.Invalid -> error("Bad credentials")
            }
        }
    }
}

private suspend fun ApplicationCall.setSessionAndRedirect(session: UserSession) {
    sessions.set(session)
    respondRedirect("/")
}
