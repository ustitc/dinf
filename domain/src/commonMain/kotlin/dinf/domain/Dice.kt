package dinf.domain

interface Dice {

    suspend fun roll(): Roll

    val name: Name<Dice>

    val edges: Edges

    class Simple(
        override val name: Name<Dice>,
        override val edges: Edges
    ) : Dice {

        constructor(name: String, edges: Edges) : this(
            name = Name.Stub(name),
            edges = edges
        )

        override suspend fun roll(): Roll {
            return Roll.Lazy(this)
        }
    }

}
