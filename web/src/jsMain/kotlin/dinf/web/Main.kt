package dinf.web

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import dinf.api.APIDices
import dinf.domain.Dices
import org.jetbrains.compose.web.css.height
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.dom.*
import org.jetbrains.compose.web.renderComposable

private val dices: Dices = APIDices("http://localhost:8080")

fun main() {
    renderComposable(rootElementId = "root") {
        var createForm by remember { mutableStateOf(false) }

        Nav(attrs = { classes("level") }) {
            Div(attrs = { classes("level-left") }) {
                Div(attrs = { classes("level-item", "has-text-centered") }) {
                    Img(src = "dinf.png", attrs = {
                        style {
                            height(50.px)
                        }
                    })
                }
                Div(attrs = { classes("level-item", "has-text-centered") }) {
                    A(attrs = {
                        classes("link", "is-info")
                        onClick {
                            createForm = true
                        }
                    }) {
                        Text("Create dice")
                    }
                }
                if (createForm) {
                    Div(attrs = { classes("modal", "is-active") }) {
                        Div(attrs = {
                            classes("modal-background")
                            onClick {
                                createForm = false
                            }
                        }) { }
                        Div(attrs = { classes("modal-content") }) {
                            P { Text("test") }
                        }
                        Button(attrs = {
                            classes("modal-close")
                            onClick {
                                createForm = false
                            }
                        }) { }
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
