package dinf.web

import androidx.compose.runtime.*
import dinf.domain.Article
import dinf.types.PInt
import dinf.domain.Articles
import org.jetbrains.compose.web.css.padding
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.dom.*
import org.jetbrains.compose.web.renderComposable

private val articles: Articles = HTTPArticles("http://localhost:8080")

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
        articles = dinf.web.articles.list(PInt(100))
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
