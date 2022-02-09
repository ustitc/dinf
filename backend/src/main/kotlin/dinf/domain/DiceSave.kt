package dinf.domain

interface DiceSave {

    suspend fun create(dice: Dice): Dice

}
