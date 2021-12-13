package dinf.backend

import dinf.domain.Dice
import dinf.domain.SerialNumber
import dinf.exposed.DiceEntity

class DBDice(entity: DiceEntity) : Dice by Dice.Simple(
    serialNumber = SerialNumber.Simple(entity.id.value),
    name = DBDiceName(entity),
    edges = DBEdges(entity)
)
