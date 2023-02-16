package dinf.domain

interface DiceOwner {

    val id: ID

    fun findDice(diceId: ID): Dice?

    fun deleteDice(diceId: ID)

    class Stub(
        override val id: ID = ID.first(),
        private val blockFind: () -> Dice? = { Dice.Stub() },
        private val blockDelete: () -> Any = {}
    ) : DiceOwner {

        override fun findDice(diceId: ID): Dice? = blockFind.invoke()

        override fun deleteDice(diceId: ID) {
            blockDelete.invoke()
        }
    }

}
