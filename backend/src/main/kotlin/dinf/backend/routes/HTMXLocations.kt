package dinf.backend.routes

import io.ktor.locations.*

@Location("/api/htmx")
class HTMXLocations {

    @Location("/search")
    data class Search(val dices: DiceLocation = DiceLocation, val query: String)

}
