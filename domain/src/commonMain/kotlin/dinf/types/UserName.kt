package dinf.types

import kotlin.jvm.JvmInline

@JvmInline
value class UserName(private val value: NotBlankString) {

    override fun toString(): String = value.toString()

    companion object {

        fun orNull(str: String): UserName? {
            return NBString.orNull(str)?.let { UserName(it) }
        }

        fun orThrow(str: String): UserName {
            return orNull(str)!!
        }
    }
}
