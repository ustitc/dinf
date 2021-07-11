package dinf.types

import kotlin.jvm.JvmInline

@JvmInline
value class JWTSubClaim(private val value: NotBlankString) {

    override fun toString(): String = value.toString()

}
