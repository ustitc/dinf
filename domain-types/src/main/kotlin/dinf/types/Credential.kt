package dinf.types

import arrow.refinement.numbers.PositiveInt

sealed interface Credential

@JvmInline
value class GoogleCredential(val id: JWTSubClaim) : Credential {

    override fun toString(): String = id.toString()

}

@JvmInline
value class GithubCredential(val id: PositiveInt) : Credential {

    fun toInt(): Int = id.value

}
