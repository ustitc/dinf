package dinf.types

import dinf.domain.Author

data class Article(
    val id: ArticleID,
    val name: NotBlankString,
    val description: String,
    val values: Values,
    val author: Author
)
