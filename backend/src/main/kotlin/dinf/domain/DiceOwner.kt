package dinf.domain

interface DiceOwner {

    val id: ID

    fun findDice(diceId: ID): Dice?

    fun deleteDice(diceId: ID)

}
