package dinf.backend.templates

import io.ktor.html.*
import kotlinx.html.FlowContent
import kotlinx.html.article
import kotlinx.html.div

class BulmaMedia : Template<FlowContent> {

    val left = Placeholder<FlowContent>()
    val content = Placeholder<FlowContent>()
    val right = Placeholder<FlowContent>()

    override fun FlowContent.apply() {
        article("media") {
            div("media-left") {
                insert(left)
            }
            div("media-content") {
                insert(content)
            }
            div("media-right") {
                insert(right)
            }
        }
    }
}
