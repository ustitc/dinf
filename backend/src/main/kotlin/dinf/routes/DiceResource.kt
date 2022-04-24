package dinf.routes

import io.ktor.resources.*
import kotlinx.serialization.Serializable

@Resource("/dices")
@Serializable
object DiceResource {

    @Serializable
    @Resource("/new")
    class New(val dices: DiceResource = DiceResource)

    @Serializable
    @Resource("/{hashID}")
    class ByHashID(val dices: DiceResource = DiceResource, val hashID: String) {

        constructor(hashID: dinf.domain.HashID) : this(hashID = hashID.print())

    }

    @Serializable
    @Resource("/edit/{hashID}")
    data class Edit(val dices: DiceResource = DiceResource, val hashID: String, val firstTime: Boolean? = null) {

        constructor(hashID: dinf.domain.HashID, firstTime: Boolean? = null) : this(
            hashID = hashID.print(),
            firstTime = firstTime
        )

    }

    @Serializable
    @Resource("/delete/{hashID}")
    data class Delete(val dices: DiceResource = DiceResource, val hashID: String)

}
