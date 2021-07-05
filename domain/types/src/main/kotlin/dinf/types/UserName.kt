package dinf.types

@JvmInline
value class UserName(private val value: NotBlankString) {

    override fun toString(): String = value.toString()

}
