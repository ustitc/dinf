package dinf.routes

import io.ktor.resources.*
import kotlinx.serialization.Serializable

@Resource("/dices")
@Serializable
object DiceLocation {

    @Serializable
    @Resource("/new")
    class New(val dices: DiceLocation = DiceLocation)

    @Serializable
    @Resource("/{hashID}")
    class ByHashID(val dices: DiceLocation = DiceLocation, val hashID: String) {

        constructor(hashID: dinf.domain.HashID) : this(hashID = hashID.print())

    }

    @Serializable
    @Resource("/edit/{hashID}")
    data class Edit(val dices: DiceLocation = DiceLocation, val hashID: String, val firstTime: Boolean? = null) {

        constructor(hashID: dinf.domain.HashID, firstTime: Boolean? = null) : this(
            hashID = hashID.print(),
            firstTime = firstTime
        )

    }

    @Serializable
    @Resource("/delete/{id}")
    data class Delete(val dices: DiceLocation = DiceLocation, val id: String)

}
