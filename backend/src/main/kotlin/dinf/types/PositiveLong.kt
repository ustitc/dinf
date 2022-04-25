package dinf.types

@JvmInline
value class PositiveLong private constructor(private val number: Long) {

    fun toLong(): Long {
        return number
    }

    companion object {

        fun fromLongOrNull(long: Long): PositiveLong? {
            return if (long > 0) {
                PositiveLong(long)
            } else {
                null
            }
        }

        fun fromLong(long: Long): PositiveLong = fromLongOrNull(long)!!

    }

}

typealias PLong = PositiveLong

fun Long.toPLongOrNull(): PLong? {
    return PositiveLong.fromLongOrNull(this)
}

fun Long.toPLong(): PLong = toPLongOrNull()!!
