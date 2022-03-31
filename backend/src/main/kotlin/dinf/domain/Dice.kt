package dinf.domain

import dinf.types.NBString

interface Dice {

    val serialNumber: SerialNumber

    val name: Name

    val edges: Edges

    class Stub(name: String = "stub") : Dice by Simple(
        serialNumber = SerialNumber.Rand(),
        name = Name.Stub(name),
        edges = Edges.Simple(listOf())
    )

    class Simple(
        override val serialNumber: SerialNumber,
        override val name: Name,
        override val edges: Edges
    ) : Dice

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
