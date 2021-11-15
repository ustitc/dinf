package dinf.web

import androidx.compose.runtime.*
import dinf.domain.Article
import dinf.domain.Articles
import dinf.domain.Dice
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import org.jetbrains.compose.web.dom.*

@Composable
fun Articles.toComposable() {
    var articles by remember { mutableStateOf(emptyList<Article>()) }

    LaunchedEffect(key1 = Unit, block = {
        articles = this@toComposable.flow().take(100).toList()
    })
    articles.map {
        it.toComposable()
    }
}

@Composable
fun Article.toComposable() {
    Article(attrs = { classes("media") }) {
        Div(attrs = { classes("media-left") }) { }
        Div(attrs = { classes("media-content") }) {
            H1(attrs = { classes("title", "has-text-link") }) {
                A(href = "") {
                    Text(this@toComposable.content.title.toString())
                }
            }
            H2(attrs = { classes("subtitle") }) {
                Text("by <unknown>")
            }
            Div(attrs = { classes("content") }) {
                P {
                    Text(this@toComposable.content.description.take(100))
                }
            }
        }
        Div(attrs = { classes("media-right") }) { }
    }
}

@Composable
fun Dice.toComposable() {
    var diceRoll by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()

    Div(attrs = { classes("block") }) {
        Button(attrs = {
            classes("button", "is-primary")
            onClick {
                scope.launch {
                    this@toComposable.roll()
                    diceRoll = this@toComposable.top.value.toString()
                }
            }
        }) {
            Text("Generate")
        }
    }
    Div(attrs = { classes("block") }) {
        Label {
            Text(diceRoll)
        }
    }
}
