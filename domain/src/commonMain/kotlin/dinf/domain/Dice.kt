package dinf.domain

import dinf.types.NBString
import kotlin.random.Random

interface Dice {

    suspend fun roll()

    val id: Int

    val name: NBString

    val top: Edge

    val edges: List<Edge>

    class Stub(
        override val id: Int,
        override val name: NBString,
        override val edges: List<Edge>
    ) : Dice {

        constructor(name: String, edges: List<Edge>) : this(Random.Default.nextInt(1000), NBString(name), edges)

        private var current = edges.first()

        override suspend fun roll() {
            current = edges.random()
        }

        override val top: Edge
            get() = current
    }

}
