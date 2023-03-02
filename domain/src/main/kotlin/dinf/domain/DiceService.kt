package dinf.domain

import kotlinx.coroutines.flow.toList

class DiceService(
    private val diceFactory: DiceFactory,
    private val diceRepository: DiceRepository
) {

    suspend fun createDice(name: Name, edges: List<Edge>, userID: ID): Dice {
        return diceFactory.create(name, edges, userID)
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

    suspend fun renameDice(id: ID, name: Name) {
        val dice = findDice(id)
        requireNotNull(dice)
        diceRepository.update(dice.copy(name = name))
    }

}
