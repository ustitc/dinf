package dinf.backend

import dinf.domain.Dice
import dinf.exposed.DiceEntity

class DBDice(entity: DiceEntity) : Dice by Dice.Simple(
    name = DBDiceName(entity),
    edges = DBEdges(entity)
)
