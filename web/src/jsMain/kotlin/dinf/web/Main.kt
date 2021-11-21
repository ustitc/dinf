package dinf.web

import dinf.api.APIDices
import dinf.domain.Dices
import org.jetbrains.compose.web.css.CSSNumeric
import org.jetbrains.compose.web.css.height
import org.jetbrains.compose.web.css.maxHeight
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.dom.*
import org.jetbrains.compose.web.renderComposable

private val dices: Dices = APIDices("http://localhost:8080")

fun main() {
    renderComposable(rootElementId = "root") {
        Nav(attrs = { classes("level") }) {
            Div(attrs = { classes("level-left") }) {
                Div(attrs = { classes("level-item", "has-text-centered") }) {
                    Img(src = "dinf.png", attrs = {
                        style {
                            height(50.px)
                        }
                    })
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
