package dinf.routes

import io.ktor.resources.*
import kotlinx.serialization.Serializable

@Serializable
@Resource("/api/htmx")
object HTMXLocations {

    @Serializable
    @Resource("/search")
    data class Search(
        val api: HTMXLocations = HTMXLocations,
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
    data class Dices(val api: HTMXLocations = HTMXLocations, val page: Int, val count: Int) {

        fun nextPage(): Dices {
            return copy(page = page + 1)
        }

    }

}
