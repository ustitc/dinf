package dinf.web

import dinf.api.APIDices
import dinf.domain.Dice
import dinf.domain.Dices
import dinf.domain.Edges
import org.jetbrains.compose.web.css.padding
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.dom.*
import org.jetbrains.compose.web.renderComposable

private val dices: Dices = APIDices("http://localhost:8080")
private val dice: Dice = Dice.Simple(
    name = "D6",
    edges = Edges.Simple(1, 2, 3, 4, 5, 6)
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
                dices.toComposable()
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
