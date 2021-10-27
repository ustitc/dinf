package dinf.types

import dinf.domain.Author
import kotlinx.datetime.Instant

data class Article(
    val id: ArticleID,
    val name: NotBlankString,
    val description: String,
    val values: Values,
    val creationTime: Instant,
    val lastUpdateTime: Instant,
    val author: Author
)
