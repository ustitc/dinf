package dinf.domain

import kotlinx.coroutines.flow.toList

class DiceService(
    private val diceFactory: DiceFactory,
    private val diceRepository: DiceRepository,
    private val publicIDFactory: PublicIDFactory,
    private val diceOwnerFactory: DiceOwnerFactory
) {

    suspend fun createDice(name: Name, edges: List<Edge>, userID: ID): PublicID {
        val dice = diceFactory.create(name, edges, userID)
        return publicIDFactory.fromID(dice.id)
    }

    suspend fun findDiceByPublicID(publicID: String): Dice? {
        val id = publicIDFactory.fromStringOrNull(publicID)
            ?.toID()
            ?: return null

        return diceRepository.oneOrNull(id)
    }

    fun findDiceByPublicIdAndUserId(publicID: String, userID: ID): Dice? {
        val diceOwner = diceOwnerFactory.create(userID)
        val diceId = publicIDFactory.fromStringOrNull(publicID)?.toID()
        if (diceId != null) {
            return diceOwner.findDice(diceId)
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

    fun deleteByPublicIdAndUserId(publicID: String, userId: ID) {
        val diceOwner = diceOwnerFactory.create(userId)
        publicIDFactory.fromStringOrNull(publicID)
            ?.toID()
            ?.let { diceOwner.deleteDice(it) }
    }

    suspend fun renameDice(publicID: String, name: Name) {
        val dice = findDiceByPublicID(publicID)
        requireNotNull(dice)
        diceRepository.update(dice.copy(name = name))
    }

}
