package dinf.routes

import dinf.auth.Credential
import dinf.auth.UserPrincipal
import dinf.auth.AuthenticationService
import dinf.auth.CredentialValidation
import dinf.auth.UserSession
import dinf.html.pages.LoginPage
import dinf.html.pages.RegisterPage
import dinf.plugins.FORM_LOGIN_CONFIGURATION_NAME
import dinf.plugins.FORM_LOGIN_EMAIL_FIELD
import dinf.plugins.FORM_LOGIN_PASSWORD_FIELD
import dinf.plugins.OAUTH_GOOGLE_CONFIGURATION_NAME
import dinf.plugins.respondPage
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.resources.*
import io.ktor.server.resources.post
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*

fun Route.loginForm() {
    get<LoginResource> {
        call.respondPage(LoginPage(it))
    }
}

fun Route.registerForm() {
    get<RegisterResource> {
        call.respondPage(RegisterPage(it))
    }
}

fun Route.login() {
    authenticate(FORM_LOGIN_CONFIGURATION_NAME) {
        post<LoginResource> {
            val principal = call.principal<UserPrincipal>()!!
            call.setSessionAndRedirect(principal.session)
        }
    }
}

fun Route.register(authSvc: AuthenticationService) {
    post<RegisterResource> {
        val params = call.receiveParameters()
        val email = params[FORM_LOGIN_EMAIL_FIELD]
        val password = params[FORM_LOGIN_PASSWORD_FIELD]
        requireNotNull(email)
        requireNotNull(password)

        val auth = Credential.EmailPassword(email, password)
        when (val result = authSvc.register(auth)) {
            is AuthenticationService.Registration.Created -> call.setSessionAndRedirect(result.userPrincipal.session)
            is AuthenticationService.Registration.Exists -> {
                val redirect = application.href(RegisterResource(userExists = true))
                call.respondRedirect(redirect)
            }
        }
    }
}

fun Route.logout() {
    get<LogoutResource> {
        call.sessions.clear<UserSession>()
        call.respondRedirect("/")
    }
}

fun Route.oAuthGoogle(authSvc: AuthenticationService) {
    authenticate(OAUTH_GOOGLE_CONFIGURATION_NAME) {
        get<OAuthResource.Google> {
            // Redirects to 'authorizeUrl' automatically
        }

        get<OAuthResource.Google.Callback> {
            val principal: OAuthAccessTokenResponse.OAuth2? = call.principal()
            requireNotNull(principal) { "Callback must contain token" }

            val auth = Credential.Google(principal.accessToken)
            when (val login = authSvc.login(auth)) {
                is CredentialValidation.Ok -> {
                    call.sessions.set(login.userPrincipal.session)
                    call.respondRedirect("/")
                }
                is CredentialValidation.Invalid -> error("Bad credentials")
                is CredentialValidation.NotFound -> {
                    val redirect = application.href(RegisterResource(userExists = true))
                    call.respondRedirect(redirect)
                }
            }
        }
    }
}

private suspend fun ApplicationCall.setSessionAndRedirect(session: UserSession) {
    sessions.set(session)
    respondRedirect("/")
}
