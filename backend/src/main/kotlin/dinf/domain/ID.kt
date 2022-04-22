package dinf.domain

import kotlin.random.Random

interface ID {

    val number: Long

    data class Simple(override val number: Long) : ID

    data class Rand(override val number: Long = Random.nextLong()) : ID

    class Empty : ID {
        override val number: Long
            get() = throw IllegalStateException("Can't compute number for empty ID")
    }

}
