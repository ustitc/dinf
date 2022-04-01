package dinf.domain

import kotlin.random.Random

typealias SN = SerialNumber

interface SerialNumber {

    val number: Long

    data class Simple(override val number: Long) : SerialNumber

    data class Rand(override val number: Long = Random.nextLong()) : SerialNumber

    class Empty : SerialNumber {
        override val number: Long
            get() = throw IllegalStateException("Can't compute number for empty serial")
    }

}
