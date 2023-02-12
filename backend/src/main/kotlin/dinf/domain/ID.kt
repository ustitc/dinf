package dinf.domain

import dinf.types.PLong
import dinf.types.toPLong

@JvmInline
value class ID(val number: PLong) {

    constructor(number: Int) : this(number.toPLong())

    companion object {

        fun first() = ID(PLong.fromLong(1L))

        fun fromLong(long: Long) = ID(long.toPLong())

    }

}
