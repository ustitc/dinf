package dinf.web

import androidx.compose.runtime.Composable
import dinf.domain.Article
import dinf.domain.Author
import dinf.domain.Content
import dinf.types.ArticleID
import dinf.types.NBString
import dinf.types.PInt
import dinf.types.Values
import dinf.usecase.ArticleUseCases
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withTimeoutOrNull
import org.jetbrains.compose.web.css.padding
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.dom.*
import org.jetbrains.compose.web.renderComposable

suspend fun main(): Unit = coroutineScope {
    val articleUC: ArticleUseCases = ArticleUseCases.Stub(
        listOf(
            Article(
                id = ArticleID(1),
                content = Content(
                    title = NBString("Dices"),
                    description = "Dices to roll",
                    values = Values("d4", "d6", "d8", "d10", "d12", "d20", "d100")
                ),
                author = Author.Stub()
            ),
            Article(
                id = ArticleID(2),
                content = Content(
                    title = NBString("Colors"),
                    description = "",
                    values = Values("red", "green", "blue", "purple", "cyan", "yellow")
                ),
                author = Author.Stub()
            )
        )
    )
    val articles = withTimeoutOrNull(2000) {
        articleUC.articles(PInt(10))
    }

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
                articles?.map {
                    GeneratorArticle(it)
                }
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
fun GeneratorArticle(article: Article) {
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
