package dinf.domain

import dinf.types.Article
import dinf.types.ArticleID

interface Author {

    fun articles(): List<Article>

    fun createArticle(content: Content): Article

    fun editArticle(articleID: ArticleID, block: Content.() -> Unit): Result<Unit>

    fun deleteArticle(articleID: ArticleID): Result<Unit>

    fun deleteArticles()

}
