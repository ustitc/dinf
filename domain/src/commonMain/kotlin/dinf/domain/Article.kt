package dinf.domain

import dinf.types.ArticleID

data class Article(
    val id: ArticleID,
    val content: Content,
    val author: Author
)
