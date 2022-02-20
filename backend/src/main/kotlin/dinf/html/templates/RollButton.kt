package dinf.html.templates

import dev.ustits.hyperscript.hyperscript
import io.ktor.html.*
import kotlinx.html.FlowContent
import kotlinx.html.a
import kotlinx.html.p
import kotlinx.html.role

class RollButton(
    private val _id: String,
    private val eventName: String
) : Template<FlowContent> {

    var buttonText = "Roll"

    override fun FlowContent.apply() {
        p {
            a {
                role = "button"
                hyperscript = "on click send $eventName to the #$_id"
                +buttonText
            }
        }
    }
}
