package dinf.domain

import dinf.types.NBString

interface Roll {

    val value: NBString

    class Lazy(values: List<NBString>) : Roll {

        constructor(dice: Dice) : this(dice.edges.map { it.value })

        override val value: NBString by lazy { values.random() }

    }

}
