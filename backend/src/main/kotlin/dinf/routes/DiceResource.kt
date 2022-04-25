package dinf.routes

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
    @Resource("/{shareID}")
    data class ByShareID(val dices: DiceResource = DiceResource, val shareID: String) {

        constructor(shareID: dinf.domain.PublicID) : this(shareID = shareID.print())

    }

    @Serializable
    @Resource("/edit/{editID}")
    data class Edit(
        val dices: DiceResource = DiceResource,
        val editID: String,
        val isFirstTime: Boolean? = null,
        val isFailed: Boolean? = null
    ) {

        constructor(editID: dinf.domain.PublicID, firstTime: Boolean? = null) : this(
            editID = editID.print(),
            isFirstTime = firstTime
        )

    }

    @Serializable
    @Resource("/delete/{editID}")
    data class Delete(val dices: DiceResource = DiceResource, val editID: String)

}
