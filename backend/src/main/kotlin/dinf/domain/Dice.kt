package dinf.domain

import dinf.types.toPLongOrNull
import kotlin.random.Random

interface Dice {

    val id: ID

    val name: Name

    val edges: Edges

    fun change(name: Name, edges: Edges)

    class Stub(name: String = "stub") : Dice by Simple(
        id = ID(Random.nextLong(1, 100).toPLongOrNull()!!),
        name = Name(name),
        edges = Edges()
    )

    class Simple(
        override val id: ID,
        override var name: Name,
        override var edges: Edges
    ) : Dice {

        override fun change(name: Name, edges: Edges) {
            this.name = name
            this.edges = edges
        }
    }

}
