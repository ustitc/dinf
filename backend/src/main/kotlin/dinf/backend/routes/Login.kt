package dinf.backend.routes

import dinf.backend.templates.BulmaColor
import dinf.backend.templates.BulmaMessage
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
import kotlinx.html.p

fun Route.loginForm(layout: Layout) {
    get<LoginLocation.Form> { form ->
        call.respondHtmlTemplate(layout) {
            content {
                if (form.fail) {
                    insert(BulmaMessage()) {
                        color = BulmaColor.IS_DANGER
                        body {
                            p { +"Wrong username or password" }
                        }
                    }
                }
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
