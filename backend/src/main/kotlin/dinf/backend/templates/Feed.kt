package dinf.backend.templates

import io.ktor.html.*
import kotlinx.html.FlowContent

class Feed : Template<FlowContent> {

    val item = PlaceholderList<FlowContent, FlowContent>()
    val noContent = Placeholder<FlowContent>()

    override fun FlowContent.apply() {
        if (!item.isEmpty()) {
            each(item) {
                insert(it)
            }
        } else {
            insert(noContent)
        }
    }
}
