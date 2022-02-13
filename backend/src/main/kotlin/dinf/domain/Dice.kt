package dinf.domain

import dinf.types.NBString

interface Dice {

    suspend fun roll(): Roll

    val serialNumber: SerialNumber

    val name: Name

    val edges: Edges

    class Stub : Dice by Simple(
        serialNumber = SerialNumber.Empty(),
        name = Name.Stub("stub"),
        edges = Edges.Simple(listOf())
    )

    class Simple(
        override val serialNumber: SerialNumber,
        override val name: Name,
        override val edges: Edges
    ) : Dice {

        override suspend fun roll(): Roll {
            return Roll.Lazy(this)
        }
    }

    class New(name: Name, edges: Edges) : Dice by Simple(
        serialNumber = SerialNumber.Empty(),
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
