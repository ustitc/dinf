package dinf.backend.templates

import io.ktor.html.*
import kotlinx.html.FlowContent
import kotlinx.html.p
import kotlinx.html.section

class Feed : Template<FlowContent> {

    val card = PlaceholderList<FlowContent, FlowContent>()

    override fun FlowContent.apply() {
        if (!card.isEmpty()) {
            each(card) {
                section {
                    insert(it)
                }
            }
        } else {
            p { +"No content" }
        }
    }
}
