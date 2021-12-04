package dinf.backend.templates

import io.ktor.html.*
import kotlinx.html.FlowContent
import kotlinx.html.div

class Feed : Template<FlowContent> {

    val card = PlaceholderList<FlowContent, BulmaMedia>()

    override fun FlowContent.apply() {
        if (!card.isEmpty()) {
            each(card) {
                div("box") {
                    insert(BulmaMedia()) {
                        insert(it)
                    }
                }
            }
        }
    }
}
