package dinf.backend.routes

import io.ktor.application.*
import io.ktor.locations.*

@Location("/dices")
object DiceLocation {

    @Location("/new")
    class New(val dices: DiceLocation = DiceLocation) {

        fun uri(call: ApplicationCall): String {
            return buildURI(call, this)
        }

    }

    @Location("/{id}")
    class ID(val dices: DiceLocation = DiceLocation, val id: String) {

        constructor(id: dinf.domain.ID) : this(id = id.print().toString())

        fun url(baseURL: String, call: ApplicationCall): String {
            return buildURL(baseURL, call, this)
        }

    }

    @Location("/edit/{id}")
    data class Edit(val dices: DiceLocation = DiceLocation, val id: String) {

        constructor(id: dinf.domain.ID) : this(id = id.print().toString())

        fun url(baseURL: String, call: ApplicationCall): String {
            return buildURL(baseURL, call, this)
        }

        fun uri(call: ApplicationCall): String {
            return buildURI(call, this)
        }

    }

    @Location("/delete/{id}")
    data class Delete(val dices: DiceLocation = DiceLocation, val id: String)

    private fun buildURI(call: ApplicationCall, location: Any): String {
        return call.locations.href(location)
    }

    private fun buildURL(baseURL: String, call: ApplicationCall, location: Any): String {
        val uri = buildURI(call, location)
        return "$baseURL$uri"
    }

}
