package dinf.usecase

import arrow.core.Either
import dinf.domain.User
import dinf.types.*

interface ArticleUseCases {

    fun showManyArticles(limit: UInt): List<Article>

    fun showArticle(id: ArticleID): Either<ArticleNotFoundError, Article>

    fun User.showOwnArticles(): List<Article>

    fun User.saveArticle(article: NewArticle): Article

    fun User.editOwnArticle(article: EditedArticle): Either<ArticleError, Article>

    fun User.deleteOwnArticle(id: ArticleID): Either<ArticleError, Unit>

    fun User.deleteAllOwnArticles()

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
