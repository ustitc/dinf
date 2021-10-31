package dinf.types

import kotlin.jvm.JvmInline

sealed interface Credential

@JvmInline
value class GoogleCredential(val id: JWTSubClaim) : Credential {

    override fun toString(): String = id.toString()

}

@JvmInline
value class GithubCredential(val id: PInt) : Credential {

    fun toInt(): Int = id.toInt()

}
