package dinf.api

import kotlinx.serialization.Serializable

@Serializable
data class AuthorDTO(
    val id: Int,
    val name: String
)
