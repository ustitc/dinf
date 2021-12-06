package dinf.domain

import dinf.types.NBString

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

        override suspend fun roll(): Roll {
            return Roll.Lazy(this)
        }
    }

    class New(name: Name<Dice>, edges: Edges) : Dice by Simple(
        id = ID.Empty(),
        name = name,
        edges = edges
    ) {

        constructor(
            name: NBString,
            edges: Edges
        ) : this(
            name = Name.Stub(name),
            edges = edges
        )

    }

}
