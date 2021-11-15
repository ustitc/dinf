package dinf.web

import dinf.domain.Articles
import dinf.domain.Dice
import dinf.domain.Edge
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
                articles.toComposable()
                dice.toComposable()
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
