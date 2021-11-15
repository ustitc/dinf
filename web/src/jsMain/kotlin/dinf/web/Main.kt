package dinf.web

import androidx.compose.runtime.*
import dinf.domain.Article
import dinf.domain.Articles
import dinf.domain.Dice
import dinf.domain.Edge
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import org.jetbrains.compose.web.css.padding
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.dom.*
import org.jetbrains.compose.web.renderComposable

private val articles: Articles = HTTPArticles("http://localhost:8080")
private val dice: Dice = Dice.Stub(
    listOf(
        Edge.Stub(1),
        Edge.Stub(2),
        Edge.Stub(3),
        Edge.Stub(4),
        Edge.Stub(5),
        Edge.Stub(6),
    )
)

fun main() {
    renderComposable(rootElementId = "root") {
        Div({
            style {
                padding(25.px)
            }
        }) {
            Header {
                Nav {
                    A {
                        Text("dInf")
                    }

                    Div {
                        Div {
                            List(4) {
                                A { Text(it.toString()) }
                            }
                        }
                        Div {
                            A { Text("Login") }
                        }
                    }
                }
            }
            Main {
                ArticlesFeed()
                Dice()
            }
            Footer(attrs = { classes("footer") }) {
                Div(attrs = { classes("container", "has-text-centered") }) {
                    P {
                        Text("by Ruslan Ustits")
                    }
                    A(href = "https://github.com/ustits") { Text("Github") }
                }
            }
        }
    }
}

@Composable
fun ArticlesFeed() {
    var articles by remember { mutableStateOf(emptyList<Article>()) }

    LaunchedEffect(key1 = Unit, block = {
        articles = dinf.web.articles.flow().take(100).toList()
    })
    articles.map {
        ArticleCard(it)
    }
}

@Composable
fun ArticleCard(article: Article) {
    Article(attrs = { classes("media") }) {
        Div(attrs = { classes("media-left") }) { }
        Div(attrs = { classes("media-content") }) {
            H1(attrs = { classes("title", "has-text-link") }) {
                A(href = "") {
                    Text(article.content.title.toString())
                }
            }
            H2(attrs = { classes("subtitle") }) {
                Text("by <unknown>")
            }
            Div(attrs = { classes("content") }) {
                P {
                    Text(article.content.description.take(100))
                }
            }
        }
        Div(attrs = { classes("media-right") }) { }
    }
}

@Composable
fun Dice() {
    var diceRoll by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()

    Div(attrs = { classes("block") }) {
        Button(attrs = {
            classes("button", "is-primary")
            onClick {
                scope.launch {
                    dice.roll()
                    diceRoll = dice.top.value.toString()
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
