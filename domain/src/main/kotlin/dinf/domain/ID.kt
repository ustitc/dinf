package dinf.domain

import dinf.types.PLong
import dinf.types.toPLong

@JvmInline
value class ID(val number: PLong) {

    constructor(number: Int) : this(number.toPLong())

    fun toLong(): Long {
        return number.toLong()
    }

    fun print(): String {
        return toLong().toString()
    }

}
