package dinf.web

import androidx.compose.runtime.Composable
import org.jetbrains.compose.web.dom.Div

@Composable
fun BulmaBlock(content: @Composable () -> Unit) {
    Div(attrs = { classes("block") }) {
        content()
    }
}
