package dinf.backend.templates

import io.ktor.html.*
import kotlinx.html.FlowContent
import kotlinx.html.HTML
import kotlinx.html.a
import kotlinx.html.body
import kotlinx.html.div
import kotlinx.html.footer
import kotlinx.html.head
import kotlinx.html.img
import kotlinx.html.link
import kotlinx.html.meta
import kotlinx.html.nav
import kotlinx.html.p
import kotlinx.html.script
import kotlinx.html.section
import kotlinx.html.style
import kotlinx.html.title

class Layout(internal val newDiceURL: String) : Template<HTML> {

    val content = Placeholder<FlowContent>()

    override fun HTML.apply() {
        head {
            title { +"dInf" }
            meta { charset = "UTF-8" }
            meta {
                name = "viewport"
                content = "width=device-width, initial-scale=1"
            }
            link(rel = "stylesheet", href = "/assets/bulma/css/bulma.min.css")
            script { src = "/assets/roll.js" }
        }
        body {
            nav("level") {
                div("level-left") {
                    div("level-item has-text-centered") {
                        a("/") {
                            img(src = "/assets/dinf.png", alt = "logo") {
                                style = "height: 50px;"
                            }
                        }
                    }
                    div("level-item has-text-centered") {
                        a(href = newDiceURL, classes = "link is-info") {
                            +"Create dice"
                        }
                    }
                }
            }
            section("section") {
                insert(content)
            }
            footer {
                div("container has-text-centered") {
                    p { +"by Ruslan Ustits" }
                    a(href = "https://github.com/ustits") { +"Github" }
                }
            }
        }
    }
}
