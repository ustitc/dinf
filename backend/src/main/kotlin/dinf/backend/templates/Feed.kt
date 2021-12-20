package dinf.backend.templates

import io.ktor.html.*
import kotlinx.html.FlowContent
import kotlinx.html.article
import kotlinx.html.p

class Feed : Template<FlowContent> {

    val card = PlaceholderList<FlowContent, FlowContent>()

    override fun FlowContent.apply() {
        if (!card.isEmpty()) {
            each(card) {
                article {
                    insert(it)
                }
            }
        } else {
            p { +"No content" }
        }
    }
}
