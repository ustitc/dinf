package dinf.types

import kotlin.jvm.JvmInline

@JvmInline
value class NotBlankString private constructor(private val value: String) {

    override fun toString(): String = value

    companion object {

        fun orNull(str: String): NotBlankString? {
            return str
                .takeIf { it.isNotBlank() }
                ?.let { NotBlankString(it) }
        }

        fun orThrow(str: String): NBString {
            return orNull(str)!!
        }
    }

}

typealias NBString = NotBlankString
