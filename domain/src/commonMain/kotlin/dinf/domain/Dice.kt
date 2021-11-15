package dinf.domain

interface Dice {

    suspend fun roll()

    val top: Edge

    val edges: List<Edge>

    class Stub(override val edges: List<Edge>) : Dice {

        private var current = edges.first()

        override suspend fun roll() {
            current = edges.random()
        }

        override val top: Edge
            get() = current
    }

}
