package dinf.backend.routes

import io.ktor.application.*
import io.ktor.locations.*

@Location("/dices")
object DiceLocation {

    @Location("/new")
    class New(val dices: DiceLocation = DiceLocation)

    @Location("/{id}")
    class ID(val dices: DiceLocation = DiceLocation, val id: String) {

        constructor(id: dinf.domain.ID) : this(id = id.print().toString())

        fun url(baseURL: String, call: ApplicationCall): String {
            return buildURL(baseURL, call, this)
        }

    }

    @Location("/edit/{id}")
    class Edit(val dices: DiceLocation = DiceLocation, val id: String) {

        constructor(id: dinf.domain.ID) : this(id = id.print().toString())

        fun url(baseURL: String, call: ApplicationCall): String {
            return buildURL(baseURL, call, this)
        }

    }

    private fun buildURL(baseURL: String, call: ApplicationCall, location: Any): String {
        val uri = call.locations.href(location)
        return "$baseURL$uri"
    }

}
