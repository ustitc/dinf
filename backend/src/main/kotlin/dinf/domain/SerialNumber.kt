package dinf.domain

import kotlin.random.Random

interface SerialNumber {

    val number: Long

    class Simple(override val number: Long) : SerialNumber

    class Rand : SerialNumber by Simple(Random.nextLong())

    class Empty : SerialNumber {
        override val number: Long
            get() = throw IllegalStateException("Can't compute number for empty serial")
    }

}
