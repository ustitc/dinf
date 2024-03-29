package dinf.app.routes

import io.ktor.resources.*
import kotlinx.serialization.Serializable

@Serializable
@Resource("/api/htmx")
object HTMXResource {

    @Serializable
    @Resource("/search")
    data class Search(
        val api: HTMXResource = HTMXResource,
        val query: String? = null,
        val page: Int,
        val count: Int
    ) {
        fun nextPage(): Search {
            return copy(page = page + 1)
        }
    }

    @Serializable
    @Resource("/dices")
    data class Dices(val api: HTMXResource = HTMXResource, val page: Int, val count: Int) {

        fun nextPage(): Dices {
            return copy(page = page + 1)
        }

    }

}
