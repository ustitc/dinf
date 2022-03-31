package dinf.domain

fun interface DiceSave : suspend (Dice) -> Dice {

    class Stub(private val list: MutableList<Dice> = mutableListOf()) : DiceSave by DiceSave({
        list.add(it)
        it
    })

}
