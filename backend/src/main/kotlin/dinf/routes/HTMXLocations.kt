package dinf.routes

import io.ktor.locations.*

@Location("/api/htmx")
object HTMXLocations {

    @Location("/search")
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

    @Location("/dices")
    data class Dices(val api: HTMXLocations = HTMXLocations, val page: Int, val count: Int) {

        fun nextPage(): Dices {
            return copy(page = page + 1)
        }

    }

}
