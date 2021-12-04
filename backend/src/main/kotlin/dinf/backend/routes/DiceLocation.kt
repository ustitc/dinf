package dinf.backend.routes

import io.ktor.locations.*

@Location("/dices")
object DiceLocation {

    @Location("/new")
    class New(val dices: DiceLocation = DiceLocation)

    @Location("/{id}")
    class ID(val dices: DiceLocation = DiceLocation, val id: String)

}
