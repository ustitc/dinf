package dinf.web

import androidx.compose.runtime.Composable
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.Footer
import org.jetbrains.compose.web.dom.Header
import org.jetbrains.compose.web.dom.P
import org.jetbrains.compose.web.dom.Section
import org.jetbrains.compose.web.dom.Text

@Composable
fun BulmaBlock(content: @Composable () -> Unit) {
    Div(attrs = { classes("block") }) {
        content()
    }
}

@Composable
fun BulmaMessage(
    header: (@Composable () -> Unit)? = null,
    color: BulmaColor? = null,
    body: @Composable () -> Unit
) {
    val messageColor = color?.text ?: ""
    Div(attrs = { classes("message", messageColor) }) {
        if (header != null) {
            Div(attrs = { classes("message-header") }) {
                header()
            }
        }
        Div(attrs = { classes("message-body") }) {
            body()
        }
    }
}

@Composable
fun BulmaModalCard(title: String, body: @Composable () -> Unit, footer: @Composable () -> Unit) {
    Div(attrs = { classes("modal-card") }) {
        Header(attrs = { classes("modal-card-head") }) {
            P(attrs = { classes("modal-card-title") }) {
                Text(title)
            }
        }
        Section(attrs = { classes("modal-card-body") }) {
            body()
        }
        Footer(attrs = { classes("modal-card-foot") }) {
            footer()
        }
    }
}
