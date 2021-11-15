package dinf.domain

import dinf.types.ArticleID
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow

interface Articles {

    suspend fun flow(): Flow<Article>

    suspend fun article(id: ArticleID): Article?

    class Stub(private val articles: List<Article>) : Articles {

        override suspend fun flow(): Flow<Article> {
            return articles.asFlow()
        }

        override suspend fun article(id: ArticleID): Article? {
            return articles.find { it.id == id }
        }

    }

}
