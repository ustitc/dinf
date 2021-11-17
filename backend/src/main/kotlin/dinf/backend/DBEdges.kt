package dinf.backend

import dinf.domain.Edges
import dinf.exposed.DiceEntity

class DBEdges(diceEntity: DiceEntity) : Edges by Edges.Simple(diceEntity.edges.toList())
