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
import org.jetbrains.compose.web.dom.Input
import org.jetbrains.compose.web.dom.Text
import org.jetbrains.compose.web.dom.TextArea

@Composable
fun CreateDiceModalCard(dices: Dices) {
    var name by remember { mutableStateOf("") }
    var edges by remember { mutableStateOf("") }
    var isError by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    val scope = rememberCoroutineScope()

    BulmaModalCard(
        title = "Create dice",
        body = {
            if (isError) {
                BulmaMessage(color = BulmaColor.IS_DANGER) {
                    Text("Error happened. Try again later. $errorMessage")
                }
            }
            FormField(name = "Name") {
                Input(InputType.Text, attrs = {
                    classes("input")
                    onInput {
                        name = it.value
                    }
                })
            }
            FormField(name = "Edges", help = "Each value must be on new line") {
                TextArea(attrs = {
                    classes("input")
                    onInput {
                        edges = it.value
                    }
                })
            }
        },
        footer = {
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
    )
}
