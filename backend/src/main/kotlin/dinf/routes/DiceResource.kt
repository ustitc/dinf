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
    @Resource("/{hashID}")
    data class ByHashID(val dices: DiceResource = DiceResource, val hashID: String) {

        constructor(hashID: dinf.domain.HashID) : this(hashID = hashID.print())

    }

    @Serializable
    @Resource("/edit/{hashID}")
    data class Edit(
        val dices: DiceResource = DiceResource,
        val hashID: String,
        val isFirstTime: Boolean? = null,
        val isFailed: Boolean? = null
    ) {

        constructor(hashID: dinf.domain.HashID, firstTime: Boolean? = null) : this(
            hashID = hashID.print(),
            isFirstTime = firstTime
        )

    }

    @Serializable
    @Resource("/delete/{hashID}")
    data class Delete(val dices: DiceResource = DiceResource, val hashID: String)

}
