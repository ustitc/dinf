package dinf.domain

interface Roll {

    val result: String

    class Lazy(edges: Edges) : Roll {

        constructor(dice: Dice) : this(dice.edges)

        override val result: String by lazy { edges.stringList.random() }

    }

}
