package dinf.domain

import dinf.types.PLong

@JvmInline
value class ID(val number: PLong) {

    companion object {

        fun first() = ID(PLong.fromLong(1L))

    }

}
