package dinf.backend

import dinf.domain.Content
import dinf.exposed.postgresTestListeners
import dinf.types.NBString
import dinf.types.Values
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import org.jetbrains.exposed.sql.transactions.transaction

class DBAuthorTest : StringSpec({

    listeners(postgresTestListeners)

    "creates a new article" {
        val entity = createUser()
        val author = DBAuthor(entity)

        author.createArticle(content())

        author.articles().size shouldBe 1
    }

    "lists all author articles" {
        val author = DBAuthor(createUser())
        author.createArticles(3)

        val articles = author.articles()

        transaction { articles.size } shouldBe 3
    }

    "lists only author's articles" {
        val author = DBAuthor(createUser())
        author.createArticles(3)
        val anotherAuthor = DBAuthor(createUser())
        anotherAuthor.createArticles(10)

        val articles = author.articles()

        transaction { articles.size } shouldBe 3
    }

    "changes article" {
        val author = DBAuthor(createUser())
        val article = author.createArticle(content())
        author.editArticle(article.id) {
            title = NBString("new title")
            description = "new description"
            values = Values("new value 1", "new value 2")
        }

        author.articles().first().content shouldBe Content(
            title = NBString("new title"),
            description = "new description",
            values = Values("new value 1", "new value 2")
        )
    }

    "returns error if tried to edit not author's article" {
        val author = DBAuthor(createUser())
        val anotherAuthor = DBAuthor(createUser())
        val article = anotherAuthor.createArticle(content())

        val result = author.editArticle(article.id) {
            title = NBString("new title")
            description = "new description"
            values = Values("new value 1", "new value 2")
        }

        result.isFailure shouldBe true
    }

    "deletes article" {
        val author = DBAuthor(createUser())
        val articleToDelete = author.createArticle(content())
        author.createArticles(10)

        author.deleteArticle(articleToDelete.id)

        author.articles().size shouldBe 10
    }

    "returns error if tried to delete not author's article" {
        val author = DBAuthor(createUser())
        val anotherAuthor = DBAuthor(createUser())
        val article = anotherAuthor.createArticle(content())

        val result = author.deleteArticle(article.id)

        result.isFailure shouldBe true
    }

    "deletes all articles" {
        val author = DBAuthor(createUser())
        author.createArticles(3)

        author.deleteArticles()

        author.articles().size shouldBe 0
    }

})
