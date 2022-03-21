package dinf.html.templates

import io.ktor.html.*
import kotlinx.html.FlowContent

interface DiceForm : Template<FlowContent> {

    var name: String
    var edges: List<String>
    var failed: Boolean

}
