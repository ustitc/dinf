package dinf.backend

import dinf.exposed.postgresTestListeners
import dinf.types.ArticleID
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import kotlinx.coroutines.flow.toList

class DBArticlesTest : StringSpec({

    listeners(postgresTestListeners)

    "lists all articles" {
        val author1 = DBAuthor(createUser())
        val author2 = DBAuthor(createUser())
        author1.createArticles(10)
        author2.createArticles(30)

        val uc = DBArticles()

        uc.flow().toList().size shouldBe 40
    }

    "finds article" {
        val author = DBAuthor(createUser())
        val article = author.createArticle(content())
        val uc = DBArticles()

        val foundArticle = uc.article(article.id)

        foundArticle shouldNotBe null
    }

    "doesn't find article if it doesn't exist" {
        val uc = DBArticles()

        val foundArticle = uc.article(ArticleID(1))

        foundArticle shouldBe null
    }

})
