package dinf.html.templates

import dinf.routes.DiceLocation
import dev.ustits.htmx.HTMXConfiguration
import dev.ustits.htmx.htmxConfiguration
import dinf.html.components.picoHyperlinkAsButton
import io.ktor.html.*
import io.ktor.locations.*
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
import kotlinx.html.script
import kotlinx.html.small
import kotlinx.html.style
import kotlinx.html.title

class Layout(private val newDiceURL: String, private val htmxConfiguration: HTMXConfiguration) : Template<HTML> {

    constructor(locations: Locations, htmxConfiguration: HTMXConfiguration) : this(
        newDiceURL = locations.href(DiceLocation.New()),
        htmxConfiguration = htmxConfiguration
    )

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

            link(rel = "stylesheet", href = "/assets/pico/css/pico.min.css")
            link(rel = "stylesheet", href = "/assets/dinf.css")
            script { src = "/assets/htmx.org/htmx.min.js" }
            script { src = "/assets/hyperscript.org/_hyperscript_web.min.js" }
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
                    end {
                        picoHyperlinkAsButton(href = newDiceURL) {
                            +"New dice"
                        }
                    }
                }
            }

            main(classes = "container") {
                insert(content)
            }
            footer("container") {
                small {
                    p { +"by Ruslan Ustits" }
                    a(href = "https://github.com/ustits") { +"Github" }
                }
            }
        }
    }
}
