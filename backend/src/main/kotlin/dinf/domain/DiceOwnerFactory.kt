package dinf.domain

interface DiceOwnerFactory {

    fun create(userID: ID): DiceOwner

    class Stub(private val diceOwner: DiceOwner = DiceOwner.Stub()) : DiceOwnerFactory {
        override fun create(userID: ID): DiceOwner = diceOwner
    }

}
