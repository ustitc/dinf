package dinf.domain

import arrow.core.Either
import dinf.types.*

interface Author {

    val id: UserID

    fun showOwnArticles(): List<Article>

    fun saveArticle(article: NewArticle): Article

    fun editOwnArticle(article: EditedArticle): Either<ArticleError, Article>

    fun deleteOwnArticle(id: ArticleID): Either<ArticleError, Unit>

    fun deleteAllOwnArticles()

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
