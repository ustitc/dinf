package dinf.html.templates

import io.ktor.html.*
import kotlinx.html.FlowContent

class Feed : Template<FlowContent> {

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
