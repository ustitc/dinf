package dinf.types

import kotlin.jvm.JvmInline

@JvmInline
value class UserName(private val value: NotBlankString) {

    override fun toString(): String = value.toString()

}
