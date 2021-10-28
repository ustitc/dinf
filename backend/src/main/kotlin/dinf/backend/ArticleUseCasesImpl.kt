package dinf.backend

import dinf.types.Article
import dinf.types.ArticleID
import dinf.usecase.ArticleUseCases

class ArticleUseCasesImpl(private val repository: ArticleRepository) : ArticleUseCases {

    override fun showManyArticles(limit: UInt): List<Article> =
        repository.findAll(limit)

    override fun showArticle(id: ArticleID): Result<Article> {
        val article = repository.findByID(id)
        return if (article != null) {
            Result.success(article)
        } else {
            Result.failure(
                ArticleNotFoundException(id)
            )
        }
    }

}
