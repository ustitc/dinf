package dinf.backend.routes

import dinf.api.ArticleDTO
import dinf.api.AuthorDTO
import dinf.domain.Article
import dinf.domain.Articles
import dinf.domain.Author
import dinf.domain.Content
import dinf.types.ArticleID
import dinf.types.NBString
import dinf.types.Values
import io.ktor.application.*
import io.ktor.locations.*
import io.ktor.response.*
import io.ktor.routing.*
import kotlinx.coroutines.flow.toList

@Location("/article")
class ArticleLocation {

    @Location("/list")
    data class List(val page: Int = 0)

}

private val articles = Articles.Stub(
    listOf(
        Article(
            id = ArticleID(1),
            content = Content(
                title = NBString("Dices"),
                description = "Dices to roll",
                values = Values("d4", "d6", "d8", "d10", "d12", "d20", "d100")
            ),
            author = Author.Stub()
        ),
        Article(
            id = ArticleID(2),
            content = Content(
                title = NBString("Colors"),
                description = "",
                values = Values("red", "green", "blue", "purple", "cyan", "yellow")
            ),
            author = Author.Stub()
        )
    )
)

fun Article.toDTO(): ArticleDTO {
    return ArticleDTO(
        id = id.toInt(),
        title = content.title.toString(),
        description = content.description,
        author = AuthorDTO(
            id = 1,
            name = "unknown"
        )
    )
}

fun Route.articles() {
    get<ArticleLocation.List> {
        val articles = articles.flow().toList()
        val response = articles.map { it.toDTO() }
        call.respond(
            response
        )
    }
}
