package dinf.usecase

import arrow.core.Either
import dinf.data.ArticleRepository
import dinf.types.Article
import dinf.types.ArticleID
import dinf.types.ArticleNotFoundError

class ArticleUseCasesImpl(private val repository: ArticleRepository) : ArticleUseCases {

    override fun showManyArticles(limit: UInt): List<Article> =
        repository.findAll(limit)

    override fun showArticle(id: ArticleID): Either<ArticleNotFoundError, Article> =
        Either.fromNullable(
            repository.findByID(id)
        ).mapLeft { ArticleNotFoundError }

}
