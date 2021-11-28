package dinf.web

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import dinf.domain.Dice
import dinf.domain.Dices
import dinf.domain.Edges
import io.ktor.client.features.*
import kotlinx.coroutines.launch
import org.jetbrains.compose.web.attributes.InputType
import org.jetbrains.compose.web.dom.Button
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.Footer
import org.jetbrains.compose.web.dom.Header
import org.jetbrains.compose.web.dom.Input
import org.jetbrains.compose.web.dom.Label
import org.jetbrains.compose.web.dom.P
import org.jetbrains.compose.web.dom.Section
import org.jetbrains.compose.web.dom.Text
import org.jetbrains.compose.web.dom.TextArea

@Composable
fun CreateDiceModalCard(dices: Dices) {
    var name by remember { mutableStateOf("") }
    var edges by remember { mutableStateOf("") }
    var isError by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    val scope = rememberCoroutineScope()

    Div(attrs = { classes("modal-card") }) {
        Header(attrs = { classes("modal-card-head") }) {
            P(attrs = { classes("modal-card-title") }) {
                Text("Create dice")
            }
        }
        Section(attrs = { classes("modal-card-body") }) {
            if (isError) {
                Div(attrs = { classes("message", "is-danger") }) {
                    Div(attrs = { classes("message-header") }) {
                        P { Text("Error") }
                        Button(attrs = {
                            classes("delete")
                            onClick {
                                isError = false
                                errorMessage = ""
                            }
                        }) { }
                    }
                    Div(attrs = { classes("message-body") }) {
                        Text("Error happened. Try again later. $errorMessage")
                    }
                }
            }
            Div(attrs = { classes("field") }) {
                Label(attrs = { classes("label") }) {
                    Text("Name")
                }
                Div(attrs = { classes("control") }) {
                    Input(InputType.Text, attrs = {
                        classes("input")
                        onInput {
                            name = it.value
                        }
                    })
                }
                Div(attrs = { classes("help") }) {
                    Text("Required field")
                }
            }
            Div(attrs = { classes("field") }) {
                Label(attrs = { classes("label") }) {
                    Text("Edges")
                }
                Div(attrs = { classes("control") }) {
                    TextArea(attrs = {
                        classes("input")
                        onInput {
                            edges = it.value
                        }
                    })
                }
                Div(attrs = { classes("help") }) {
                    Text("Required field")
                }
            }
        }

        Footer(attrs = { classes("modal-card-foot") }) {
            Button(
                attrs = {
                    classes("button")
                    onClick {
                        scope.launch {
                            val dice = Dice.Simple(
                                name = name,
                                edges = Edges.Simple(
                                    stringList = edges.split("\n")
                                )
                            )
                            try {
                                dices.create(dice)
                            } catch (ex: ClientRequestException) {
                                isError = true
                                errorMessage = ex.message
                            }
                            name = ""
                            edges = ""
                        }
                    }
                }
            ) {
                Text("Save")
            }
        }
    }
}
