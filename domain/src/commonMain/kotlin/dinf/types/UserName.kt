package dinf.types

import kotlin.jvm.JvmInline

@JvmInline
value class UserName(private val value: NBString) {

    constructor(str: String) : this(NBString(str))

    override fun toString(): String = value.toString()

}
