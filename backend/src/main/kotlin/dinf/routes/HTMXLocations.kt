package dinf.routes

import io.ktor.locations.*

@Location("/api/htmx")
object HTMXLocations {

    @Location("/search")
    data class Search(val api: HTMXLocations = HTMXLocations, val query: String? = null)

    @Location("/dices")
    data class Dices(val api: HTMXLocations = HTMXLocations, val page: Int, val count: Int)

}
