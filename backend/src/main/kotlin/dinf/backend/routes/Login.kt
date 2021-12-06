package dinf.backend.routes

import dinf.backend.templates.Form
import dinf.backend.templates.Layout
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.html.*
import io.ktor.locations.*
import io.ktor.locations.post
import io.ktor.response.*
import io.ktor.routing.Route
import kotlinx.html.InputType
import kotlinx.html.input

fun Route.loginForm(layout: Layout) {
    get<LoginLocation> {
        call.respondHtmlTemplate(layout) {
            content {
                insert(Form(loginURL)) {
                    field {
                        name = "Username"
                        control {
                            input(classes = "input", type = InputType.text, name = "username") {
                                placeholder = "megakiller_3000"
                            }
                        }
                    }
                    field {
                        name = "Password"
                        control {
                            input(classes = "input", type = InputType.password, name = "password") {
                                placeholder = "*******"
                            }
                        }
                    }
                }
            }
        }
    }
}

fun Route.login() {
    authenticate("login-form") {
        post<LoginLocation> {
            call.respondRedirect("/")
        }
    }
}
