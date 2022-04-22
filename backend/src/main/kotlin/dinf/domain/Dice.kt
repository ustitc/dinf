package dinf.domain

interface Dice {

    val serialNumber: SerialNumber

    val name: Name

    val edges: Edges

    fun change(name: Name, edges: Edges)

    class Stub(name: String = "stub") : Dice by Simple(
        serialNumber = SerialNumber.Rand(),
        name = Name(name),
        edges = Edges.Simple(listOf())
    )

    class Simple(
        override val serialNumber: SerialNumber,
        override var name: Name,
        override var edges: Edges
    ) : Dice {

        override fun change(name: Name, edges: Edges) {
            this.name = name
            this.edges = edges
        }
    }

    class New(name: Name, edges: Edges) : Dice by Simple(
        serialNumber = SerialNumber.Empty(),
        name = name,
        edges = edges
    )

}
