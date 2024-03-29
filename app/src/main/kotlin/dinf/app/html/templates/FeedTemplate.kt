package dinf.app.html.templates

import io.ktor.server.html.*
import kotlinx.html.FlowContent

class FeedTemplate : Template<FlowContent> {

    val item = PlaceholderList<FlowContent, FlowContent>()
    val pagination = Placeholder<FlowContent>()
    val noContent = Placeholder<FlowContent>()

    override fun FlowContent.apply() {
        if (!item.isEmpty()) {
            each(item) {
                insert(it)
            }
            insert(pagination)
        } else {
            insert(noContent)
        }
    }
}
