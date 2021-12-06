package dinf.backend.templates

import io.ktor.html.*
import kotlinx.html.FlowContent
import kotlinx.html.div
import kotlinx.html.nav

class BulmaNavbar : Template<FlowContent> {

    val brand = Placeholder<FlowContent>()
    val start = PlaceholderList<FlowContent, FlowContent>()
    val end = PlaceholderList<FlowContent, FlowContent>()

    override fun FlowContent.apply() {
        nav("navbar") {
            div("navbar-brand") {
                insert(brand)
            }
            div("navbar-menu") {
                div("navbar-start") {
                    each(start) {
                        div("navbar-item") {
                            insert(it)
                        }
                    }
                }
                div("navbar-end") {
                    each(end) {
                        div("navbar-item") {
                            insert(it)
                        }
                    }
                }
            }
        }
    }
}
