package dinf.adapters

import dinf.domain.Dice
import dinf.domain.SerialNumber
import dinf.exposed.DiceEntity

class DBDice(entity: DiceEntity) : Dice by Dice.Simple(
    serialNumber = SerialNumber.Simple(entity.id.value),
    name = DBName(entity),
    edges = DBEdges(entity)
)
