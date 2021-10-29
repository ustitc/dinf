package dinf.usecase

import dinf.types.Article
import dinf.types.ArticleID
import dinf.types.PInt

interface ArticleUseCases {

    fun articles(limit: PInt): List<Article>

    fun article(id: ArticleID): Result<Article>

}
