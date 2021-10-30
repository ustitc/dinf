package dinf.web

import dinf.domain.Author
import dinf.domain.Content
import dinf.types.ArticleID
import dinf.types.NBString
import dinf.types.PInt
import dinf.types.Values
import dinf.usecase.ArticleUseCases
import org.jetbrains.compose.web.css.padding
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.dom.*
import org.jetbrains.compose.web.renderComposable

fun main() {
    val articleUC: ArticleUseCases = ArticleUseCases.Stub(
        listOf(
            dinf.domain.Article(
                id = ArticleID.orThrow(1),
                content = Content(
                    title = NBString.orThrow("Dices"),
                    description = "Dices to roll",
                    values = Values.orThrow("d4", "d6", "d8", "d10", "d12", "d20", "d100")
                ),
                author = Author.Stub()
            ),
            dinf.domain.Article(
                id = ArticleID.orThrow(2),
                content = Content(
                    title = NBString.orThrow("Colors"),
                    description = "",
                    values = Values.orThrow("red", "green", "blue", "purple", "cyan", "yellow")
                ),
                author = Author.Stub()
            )
        )
    )

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
                articleUC
                    .articles(PInt.orThrow(10))
                    .map {
                        Article(attrs = { classes("media") }) {
                            Div(attrs = { classes("media-left") }) { }
                            Div(attrs = { classes("media-content") }) {
                                H1(attrs = { classes("title", "has-text-link") }) {
                                    A(href = "") {
                                        Text(it.content.title.toString())
                                    }
                                }
                                H2(attrs = { classes("subtitle") }) {
                                    Text("by <unknown>")
                                }
                                Div(attrs = { classes("content") }) {
                                    P {
                                        Text(it.content.description.take(100))
                                    }
                                }
                            }
                            Div(attrs = { classes("media-right") }) { }
                        }
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
