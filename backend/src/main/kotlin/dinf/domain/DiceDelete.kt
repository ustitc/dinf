package dinf.domain

fun interface DiceDelete : suspend (Dice) -> Unit {

    class Stub : DiceDelete by DiceDelete({})

}
