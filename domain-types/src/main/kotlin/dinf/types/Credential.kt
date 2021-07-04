package dinf.types

import arrow.refinement.numbers.PositiveInt

sealed interface Credential

@JvmInline
value class GoogleCredential(val id: JWTSubClaim) : Credential

@JvmInline
value class GithubCredential(val id: PositiveInt) : Credential
