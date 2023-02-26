package dinf.app.services

import dinf.app.auth.UserSession
import dinf.domain.DiceService
import dinf.domain.Edge
import dinf.domain.ID
import dinf.domain.Name
import dinf.domain.PublicID
import dinf.types.toNBStringOrNull
import dinf.types.toPLong
import io.ktor.http.*

class DicePageService(private val diceService: DiceService) {

    suspend fun createDice(session: UserSession, parameters: Parameters): PublicID {
        val parsedParams = fromParametersOrNull(parameters)
        requireNotNull(parsedParams)

        return diceService.createDice(parsedParams.name, parsedParams.edges, ID(session.id.toPLong()))
    }

    suspend fun updateDice(diceId: String, session: UserSession, parameters: Parameters) {
        val dice = diceService.findDiceByPublicIdAndUserId(diceId, ID(session.id.toPLong()))
        val parsedParams = fromParametersOrNull(parameters)
        requireNotNull(dice)
        requireNotNull(parsedParams)

        diceService.renameDice(diceId, parsedParams.name)
        dice.edges.replaceAll(parsedParams.edges)
    }

    private class HTMLParamsDice(val name: Name, val edges: List<Edge>)

    private fun fromParametersOrNull(parameters: Parameters): HTMLParamsDice? {
        val name = parameters["name"]?.toNBStringOrNull()
        val edges = parameters.getAll("edges")
            ?.filter { it.isNotBlank() }
            ?.map { Edge(ID.first(), it) }
            ?: emptyList()
        return if (name == null) {
            null
        } else {
            HTMLParamsDice(Name(name), edges)
        }
    }
}
