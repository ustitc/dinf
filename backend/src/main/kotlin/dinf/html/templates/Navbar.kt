package dinf.html.templates

import io.ktor.server.html.*
import kotlinx.html.FlowContent
import kotlinx.html.li
import kotlinx.html.nav
import kotlinx.html.ul

class Navbar : Template<FlowContent> {

    val start = PlaceholderList<FlowContent, FlowContent>()
    val end = PlaceholderList<FlowContent, FlowContent>()

    override fun FlowContent.apply() {
        nav {
            if (!start.isEmpty()) {
                ul {
                    each(start) {
                        this@ul.li {
                            insert(it)
                        }
                    }
                }
            }
            if (!end.isEmpty()) {
                ul {
                    each(end) {
                        this@ul.li {
                            insert(it)
                        }
                    }
                }
            }
        }
    }
}
