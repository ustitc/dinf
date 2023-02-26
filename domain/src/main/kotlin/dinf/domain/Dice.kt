package dinf.domain

interface Dice {

    val id: ID
    val name: Name
    val edges: Edges

    fun rename(name: Name)

    class Simple(
        override val id: ID,
        override var name: Name,
        override var edges: Edges
    ) : Dice {

        override fun rename(name: Name) {
            this.name = name
        }
    }

}
