package dinf.domain

import kotlin.random.Random

interface Dice {

    suspend fun roll(): Roll

    val id: Int

    val name: Name<Dice>

    val edges: Edges

    class Simple(
        override val id: Int,
        override val name: Name<Dice>,
        override val edges: Edges
    ) : Dice {

        constructor(name: String, edges: Edges) : this(
            id = Random.Default.nextInt(1000),
            name = Name.Stub(name),
            edges = edges
        )

        override suspend fun roll(): Roll {
            return Roll.Lazy(this)
        }
    }

}
