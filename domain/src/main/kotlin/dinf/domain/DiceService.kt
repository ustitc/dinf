package dinf.domain

import kotlinx.coroutines.flow.toList

class DiceService(
    private val diceRepository: DiceRepository,
    private val edgeRepository: EdgeRepository
) {

    fun createDice(request: DiceCreateRequest): Dice {
        val createdDice = diceRepository.create(request.newDice())
        val edges = request.newEdges(createdDice.id)
        val createdEdges = edgeRepository.createAll(edges)
        return createdDice.copy(edges = createdEdges)
    }

    fun findDice(id: ID): Dice? {
        return diceRepository.oneOrNull(id)
    }

    fun findDice(id: ID, userID: ID): Dice? {
        val dice = diceRepository.oneOrNull(id) ?: return null
        if (dice.ownerId == userID) {
            return dice
        }
        return null
    }

    suspend fun find(page: Page, count: Count): List<Dice> {
        return diceRepository.flow()
            .toList()
            .paginate(page, count)
    }

    fun search(query: SearchQuery): List<Dice> {
        return diceRepository
            .search(query.text)
            .paginate(query.page, query.count)
    }

    private fun <T> List<T>.paginate(page: Page, count: Count): List<T> {
        val offset = Offset(page, count)
        return drop(offset.toInt()).take(count.toInt())
    }

    fun deleteDice(diceId: ID, userId: ID) {
        val dice = diceRepository.oneOrNull(diceId)
        requireNotNull(dice)
        check(dice.ownerId == userId)
        diceRepository.remove(dice)
    }

    fun updateDice(request: DiceUpdateRequest) {
        val dice = findDice(request.diceId, request.ownerID)
        check(dice != null)
        diceRepository.update(dice.copy(name = request.toUpdate.name))
        edgeRepository.deleteAllByDiceId(request.diceId)
        edgeRepository.createAll(request.newEdges)
    }
}
