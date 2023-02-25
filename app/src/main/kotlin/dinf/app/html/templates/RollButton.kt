package dinf.app.html.templates

import dev.ustits.hyperscript.hyperscript
import dinf.app.html.components.picoInlineButton
import io.ktor.server.html.*
import kotlinx.html.FlowContent
import kotlinx.html.p

class RollButton(
    private val _id: String,
    private val eventName: String
) : Template<FlowContent> {

    var buttonText = "Roll"

    override fun FlowContent.apply() {
        p {
            picoInlineButton {
                hyperscript = "on click send $eventName to the #$_id"
                +buttonText
            }
        }
    }
}
