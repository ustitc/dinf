package dinf.app.services

import dinf.app.auth.UserSession
import dinf.domain.Dice
import dinf.domain.DiceCreateRequest
import dinf.domain.DiceService
import dinf.domain.DiceUpdateRequest
import dinf.domain.ID
import dinf.domain.Name
import dinf.types.toNBStringOrNull
import dinf.types.toPLong
import io.ktor.http.*
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class DicePageService(
    private val diceService: DiceService,
    private val diceIdFactory: PublicIDFactory,
    private val edgeIdFactory: PublicIDFactory,
    private val userIdFactory: PublicIDFactory
) {

    private val logger: Logger = LoggerFactory.getLogger(DiceService::class.java)

    fun findDice(publicDiceId: String, session: UserSession? = null): DiceView? {
        val id = diceIdFactory.fromStringOrNull(publicDiceId)
            ?.toID()
            ?: return null
        val dice = if (session == null) {
            diceService.findDice(id)
        } else {
            diceService.findDice(id, ID(session.id.toPLong()))
        }
        return dice?.toDiceView()
    }

    fun createDice(session: UserSession, parameters: Parameters): DiceView {
        val parsedParams = fromParametersOrNull(parameters)
        requireNotNull(parsedParams)

        val dice = diceService.createDice(
            DiceCreateRequest(
                name = parsedParams.name,
                edges = parsedParams.edges,
                ownerId = ID(session.id.toPLong())
            )
        )
        logger.info("Created dice=$dice")
        return dice.toDiceView()
    }

    fun updateDice(publicDiceId: String, session: UserSession, parameters: Parameters) {
        val diceId = diceIdFactory.fromStringOrNull(publicDiceId)?.toID()
        requireNotNull(diceId)
        val parsedParams = fromParametersOrNull(parameters)
        requireNotNull(parsedParams)

        diceService.updateDice(
            DiceUpdateRequest(
                diceId = diceId,
                ownerID = ID(session.id.toPLong()),
                toUpdate = DiceUpdateRequest.ToUpdate(
                    name = parsedParams.name,
                    edges = parsedParams.edges
                )
            )
        )
        logger.info("Updated dice for session=$session, params=$parameters, publicId=$publicDiceId")
    }

    fun deleteDice(publicId: String, session: UserSession) {
        diceIdFactory.fromStringOrNull(publicId)
            ?.toID()
            ?.let { diceService.deleteDice(it, ID(session.id.toPLong())) }
    }

    private class HTMLParamsDice(val name: Name, val edges: List<String>)

    private fun fromParametersOrNull(parameters: Parameters): HTMLParamsDice? {
        val name = parameters["name"]?.toNBStringOrNull()
        val edges = parameters.getAll("edges")
            ?.filter { it.isNotBlank() }
            ?: emptyList()
        return if (name == null) {
            null
        } else {
            HTMLParamsDice(Name(name), edges)
        }
    }

    private fun Dice.toDiceView(): DiceView {
        val edgeViewList = edges.map { EdgeView(id = edgeIdFactory.fromID(it.id), value = it.value) }
        return DiceView(
            id = diceIdFactory.fromID(id),
            name = name.print(),
            ownerId = userIdFactory.fromID(ownerId),
            edges = edgeViewList
        )
    }
}
