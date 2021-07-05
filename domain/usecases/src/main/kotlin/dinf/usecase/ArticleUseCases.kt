package dinf.usecase

import arrow.core.Either
import dinf.types.*

interface ArticleUseCases {

    fun User.showManyArticles(limit: UInt): List<Article>

    fun User.showArticle(id: ArticleID): Either<ArticleNotFoundError, Article>

    fun RegisteredUser.showOwnArticles(): List<Article>

    fun RegisteredUser.saveArticle(article: NewArticle): Article

    fun RegisteredUser.editOwnArticle(article: EditedArticle): Either<ArticleError, Article>

    fun RegisteredUser.deleteOwnArticle(id: ArticleID): Either<ArticleError, Unit>

    fun RegisteredUser.deleteAllOwnArticles()

    fun AdminUser.editArticle(article: EditedArticle): Either<ArticleNotFoundError, Article>

    fun AdminUser.deleteArticle(id: ArticleID): Either<ArticleNotFoundError, Unit>

}

data class NewArticle(
    val name: NotBlankString,
    val description: String,
    val generator: Generator
)

data class EditedArticle(
    val id: ArticleID,
    val name: NotBlankString,
    val description: String,
    val generator: Generator
)
