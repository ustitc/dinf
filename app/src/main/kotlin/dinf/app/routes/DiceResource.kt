package dinf.app.routes

import dinf.app.services.PublicID
import io.ktor.resources.*
import kotlinx.serialization.Serializable

@Resource("/dices")
@Serializable
object DiceResource {

    @Serializable
    @Resource("/new")
    data class New(
        val dices: DiceResource = DiceResource,
        val isFailed: Boolean? = null
    )

    @Serializable
    @Resource("/{diceID}")
    data class ByID(val dices: DiceResource = DiceResource, val diceID: String) {

        constructor(diceID: PublicID) : this(diceID = diceID.print())

    }

    @Serializable
    @Resource("/edit/{diceID}")
    data class Edit(
        val dices: DiceResource = DiceResource,
        val diceID: String,
        val isFailed: Boolean? = null
    )

    @Serializable
    @Resource("/delete/{diceID}")
    data class Delete(val dices: DiceResource = DiceResource, val diceID: String)

}
