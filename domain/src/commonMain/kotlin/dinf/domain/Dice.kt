package dinf.domain

interface Dice {

    suspend fun roll(): Roll

    val id: ID

    val name: Name<Dice>

    val edges: Edges

    class Simple(
        override val id: ID,
        override val name: Name<Dice>,
        override val edges: Edges
    ) : Dice {

        constructor(
            id: ID = ID.Empty(),
            name: String,
            edges: Edges
        ) : this(
            id = id,
            name = Name.Stub(name),
            edges = edges
        )

        override suspend fun roll(): Roll {
            return Roll.Lazy(this)
        }
    }

}
