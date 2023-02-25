package dinf.routes

import dinf.domain.PublicID
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
        val isFirstTime: Boolean? = null,
        val isFailed: Boolean? = null
    ) {

        constructor(diceID: PublicID, firstTime: Boolean? = null) : this(
            diceID = diceID.print(),
            isFirstTime = firstTime
        )

    }

    @Serializable
    @Resource("/delete/{diceID}")
    data class Delete(val dices: DiceResource = DiceResource, val diceID: String)

}
