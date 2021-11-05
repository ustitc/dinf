package dinf.web

import androidx.compose.runtime.*
import dinf.api.ArticleDTO
import dinf.domain.Article
import dinf.domain.Author
import dinf.domain.Content
import dinf.types.ArticleID
import dinf.types.NBString
import dinf.types.Values
import io.ktor.client.*
import io.ktor.client.features.json.*
import io.ktor.client.request.*
import kotlinx.coroutines.*
import org.jetbrains.compose.web.css.padding
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.dom.*
import org.jetbrains.compose.web.renderComposable

private val client = HttpClient {
    install(JsonFeature)
}

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
        articles =
            withTimeoutOrNull(2000) {
                client.get<List<ArticleDTO>>(urlString = "http://localhost:8080/article/list")
            }?.map {
                Article(
                    id = ArticleID(it.id),
                    content = Content(
                        title = NBString(it.title),
                        description = it.description,
                        values = Values("red", "green", "blue", "purple", "cyan", "yellow")
                    ),
                    author = Author.Stub()
                )
            }!!
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
