package dinf.domain

interface Dice {

    val id: ID

    val name: Name

    val edges: Edges

    fun change(name: Name, edges: Edges)

    class Stub(name: String = "stub") : Dice by Simple(
        id = ID.Rand(),
        name = Name(name),
        edges = Edges.Simple(listOf())
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

    class New(name: Name, edges: Edges) : Dice by Simple(
        id = ID.Empty(),
        name = name,
        edges = edges
    )

}
