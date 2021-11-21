package dinf.web

import dinf.api.APIDices
import dinf.domain.Dices
import org.jetbrains.compose.web.dom.*
import org.jetbrains.compose.web.renderComposable

private val dices: Dices = APIDices("http://localhost:8080")

fun main() {
    renderComposable(rootElementId = "root") {
        Nav(attrs = { classes("level") }) {
            Div(attrs = { classes("level-left") }) {
                Div(attrs = { classes("level-item") }) {
                    A {
                        Text("dInf")
                    }
                }
            }
        }
        Section(attrs = { classes("section") }) {
            dices.toComposable()
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
