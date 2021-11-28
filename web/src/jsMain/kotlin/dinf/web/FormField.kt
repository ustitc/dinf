package dinf.web

import androidx.compose.runtime.Composable
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.Label
import org.jetbrains.compose.web.dom.Text

@Composable
fun FormField(name: String, help: String? = null, content: @Composable () -> Unit) {
    Div(attrs = { classes("field") }) {
        Label(attrs = { classes("label") }) {
            Text(name)
        }
        Div(attrs = { classes("control") }) {
            content()
        }
        if (help != null) {
            Div(attrs = { classes("help") }) {
                Text(help)
            }
        }
    }
}
