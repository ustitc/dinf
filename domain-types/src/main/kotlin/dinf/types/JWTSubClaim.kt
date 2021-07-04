package dinf.types

@JvmInline
value class JWTSubClaim(private val value: NotBlankString) {

    override fun toString(): String = value.toString()

}
