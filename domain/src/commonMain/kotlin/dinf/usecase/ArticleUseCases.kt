package dinf.usecase

import dinf.types.Article
import dinf.types.ArticleID

interface ArticleUseCases {

    fun showManyArticles(limit: UInt): List<Article>

    fun showArticle(id: ArticleID): Result<Article>

}
