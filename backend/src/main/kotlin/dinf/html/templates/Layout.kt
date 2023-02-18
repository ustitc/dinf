package dinf.html.templates

import dev.ustits.htmx.HTMXConfiguration
import dev.ustits.htmx.htmxConfiguration
import dinf.auth.UserSession
import dinf.html.components.picoHyperlinkAsButton
import io.ktor.server.html.*
import kotlinx.html.FlowContent
import kotlinx.html.HTML
import kotlinx.html.a
import kotlinx.html.body
import kotlinx.html.div
import kotlinx.html.footer
import kotlinx.html.head
import kotlinx.html.img
import kotlinx.html.link
import kotlinx.html.main
import kotlinx.html.meta
import kotlinx.html.p
import kotlinx.html.role
import kotlinx.html.script
import kotlinx.html.small
import kotlinx.html.style
import kotlinx.html.title

class Layout(
    private val newDiceURL: String,
    private val htmxConfiguration: HTMXConfiguration,
    val loginURL: String,
    val logoutURL: String,
    val registerURL: String,
    val userSession: UserSession?,
    val showUserButtons: Boolean
) : Template<HTML> {

    val content = Placeholder<FlowContent>()

    override fun HTML.apply() {
        head {
            title { +"dInf" }
            meta { charset = "UTF-8" }
            meta {
                name = "viewport"
                content = "width=device-width, initial-scale=1"
            }
            htmxConfiguration(htmxConfiguration)

            link(rel = "stylesheet", href = "/assets/picocss__pico/css/pico.min.css")
            link(rel = "stylesheet", href = "/assets/dinf.css")
            script { src = "/assets/htmx.org/htmx.min.js" }
            script { src = "/assets/hyperscript.org/_hyperscript.min.js" }
            script { src = "/assets/htmx_events.js" }
        }
        body {
            div("container-fluid") {
                insert(Navbar()) {
                    start {
                        a(href = "/") {
                            img(src = "/assets/dinf.png", alt = "logo") {
                                style = "height: 50px;"
                            }
                        }
                    }
                    if (showUserButtons && userSession == null) {
                        end {
                            a(classes = "outline", href = loginURL) {
                                role = "button"
                                +"Login"
                            }
                        }
                        end {
                            a(href = registerURL) {
                                role = "button"
                                +"Register"
                            }
                        }
                    } else if (showUserButtons) {
                        end {
                            picoHyperlinkAsButton(href = newDiceURL) {
                                +"New dice"
                            }
                        }
                        end {
                            a(classes = "secondary outline", href = logoutURL) {
                                role = "button"
                                +"Logout"
                            }
                        }
                    }
                }
            }

            main(classes = "container") {
                insert(content)
            }
            footer("container-fluid") {
                small {
                    p { +"by Ruslan Ustits" }
                    a(href = "https://github.com/ustits/dinf") { +"Github" }
                }
            }
        }
    }
}
