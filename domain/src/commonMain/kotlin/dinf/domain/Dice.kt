package dinf.domain

import kotlin.random.Random

interface Dice {

    suspend fun roll(): Roll

    val id: Int

    val name: Name<Dice>

    val edges: List<Edge>

    class Stub(
        override val id: Int,
        override val name: Name<Dice>,
        override val edges: List<Edge>
    ) : Dice {

        constructor(name: String, edges: List<Edge>) : this(Random.Default.nextInt(1000), Name.Stub(name), edges)

        override suspend fun roll(): Roll {
            return Roll.Lazy(this)
        }
    }

}
