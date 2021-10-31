package dinf.domain

import dinf.types.ArticleID

interface Author {

    suspend fun articles(): List<Article>

    suspend fun createArticle(content: Content): Article

    suspend fun editArticle(articleID: ArticleID, block: Content.() -> Unit): Result<Unit>

    suspend fun deleteArticle(articleID: ArticleID): Result<Unit>

    suspend fun deleteArticles()

    class Stub(private val articles: MutableList<Article> = mutableListOf()) : Author {

        private var counter = 0

        override suspend fun articles(): List<Article> {
            return articles
        }

        override suspend fun createArticle(content: Content): Article {
            val article = Article(
                id = ArticleID(counter),
                content = content,
                author = this
            )
            counter += 1
            articles.add(article)
            return article
        }

        override suspend fun editArticle(articleID: ArticleID, block: Content.() -> Unit): Result<Unit> {
            val article = findByID(articleID)
            return if (article != null) {
                block(article.content)
                Result.success(Unit)
            } else {
                Result.failure(RuntimeException())
            }
        }

        override suspend fun deleteArticle(articleID: ArticleID): Result<Unit> {
            val article = findByID(articleID)
            return if (article != null) {
                articles.remove(article)
                Result.success(Unit)
            } else {
                Result.failure(RuntimeException())
            }
        }

        override suspend fun deleteArticles() {
            articles.clear()
        }

        private fun findByID(articleID: ArticleID): Article? {
            return articles.find { it.id == articleID }
        }
    }

}
