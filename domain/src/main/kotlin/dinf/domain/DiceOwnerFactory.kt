package dinf.domain

interface DiceOwnerFactory {

    fun create(userID: ID): DiceOwner

}
