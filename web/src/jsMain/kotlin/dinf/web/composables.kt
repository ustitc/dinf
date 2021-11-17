package dinf.web

import androidx.compose.runtime.*
import dinf.domain.Dice
import dinf.domain.Dices
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import org.jetbrains.compose.web.dom.*

@Composable
fun Dices.toComposable() {
    var dices by remember { mutableStateOf(emptyList<Dice>()) }

    LaunchedEffect(key1 = Unit, block = {
        dices = this@toComposable.flow().toList()
    })
    dices.map {
        it.card()
    }
}

@Composable
fun Dice.card() {
    Article(attrs = { classes("media") }) {
        Div(attrs = { classes("media-left") }) { }
        Div(attrs = { classes("media-content") }) {
            H1(attrs = { classes("title", "has-text-link") }) {
                A(href = "") {
                    Text(this@card.name.nbString.toString())
                }
            }
            H2(attrs = { classes("subtitle") }) {
                Text("by <unknown>")
            }
        }
        Div(attrs = { classes("media-right") }) { }
    }
}

@Composable
fun Dice.toComposable() {
    var diceRoll by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()

    H1(attrs = { classes("title") }) {
        Text(this@toComposable.name.nbString.toString())
    }
    Div(attrs = { classes("block") }) {
        Button(attrs = {
            classes("button", "is-primary")
            onClick {
                scope.launch {
                    val roll = this@toComposable.roll()
                    diceRoll = roll.result
                }
            }
        }) {
            Text("Generate")
        }
    }
    Div(attrs = { classes("block") }) {
        Label {
            Text(diceRoll)
        }
    }
}
