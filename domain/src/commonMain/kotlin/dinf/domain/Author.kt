package dinf.domain

import dinf.types.ArticleID

interface Author {

    fun articles(): List<Article>

    fun createArticle(content: Content): Article

    fun editArticle(articleID: ArticleID, block: Content.() -> Unit): Result<Unit>

    fun deleteArticle(articleID: ArticleID): Result<Unit>

    fun deleteArticles()

    class Stub(private val articles: MutableList<Article> = mutableListOf()) : Author {

        private var counter = 0

        override fun articles(): List<Article> {
            return articles
        }

        override fun createArticle(content: Content): Article {
            val article = Article(
                id = ArticleID.orThrow(counter),
                content = content,
                author = this
            )
            counter += 1
            articles.add(article)
            return article
        }

        override fun editArticle(articleID: ArticleID, block: Content.() -> Unit): Result<Unit> {
            val article = findByID(articleID)
            return if (article != null) {
                block(article.content)
                Result.success(Unit)
            } else {
                Result.failure(RuntimeException())
            }
        }

        override fun deleteArticle(articleID: ArticleID): Result<Unit> {
            val article = findByID(articleID)
            return if (article != null) {
                articles.remove(article)
                Result.success(Unit)
            } else {
                Result.failure(RuntimeException())
            }
        }

        override fun deleteArticles() {
            articles.clear()
        }

        private fun findByID(articleID: ArticleID): Article? {
            return articles.find { it.id == articleID }
        }
    }

}
