package dinf.api

import kotlinx.serialization.Serializable

@Serializable
data class ArticleDTO(
    val id: Int,
    val title: String,
    val description: String,
    val author: AuthorDTO
)
