package dinf.usecase

import arrow.core.Either
import dinf.types.Article
import dinf.types.ArticleID
import dinf.types.ArticleNotFoundError

interface ArticleUseCases {

    fun showManyArticles(limit: UInt): List<Article>

    fun showArticle(id: ArticleID): Either<ArticleNotFoundError, Article>

}
