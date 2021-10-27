package dinf.domain

import arrow.core.Either
import dinf.types.*

interface Author {

    val id: UserID

    fun articles(): List<Article>

    fun create(article: NewArticle): Article

    fun edit(article: EditedArticle): Either<ArticleError, Article>

    fun delete(id: ArticleID): Either<ArticleError, Unit>

    fun deleteArticles()

}

data class NewArticle(
    val name: NotBlankString,
    val description: String,
    val values: Values
)

data class EditedArticle(
    val id: ArticleID,
    val name: NotBlankString,
    val description: String,
    val values: Values
)
