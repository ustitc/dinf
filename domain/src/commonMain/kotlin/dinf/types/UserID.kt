package dinf.types

import kotlin.jvm.JvmInline

@JvmInline
value class UserID(private val value: PositiveInt) {

    fun toInt(): Int = value.value

    override fun toString(): String {
        return "UserID(value=${toInt()})"
    }

    companion object {

        fun orNull(int: Int): UserID? = when (val pInt = int.toPositiveInt()) {
            null -> null
            else -> UserID(pInt)
        }

    }

}
