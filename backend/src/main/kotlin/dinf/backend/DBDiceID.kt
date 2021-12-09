package dinf.backend

import dinf.domain.ID
import dinf.exposed.DiceEntity

class DBDiceID(entity: DiceEntity) : ID by ID.Serial(entity.id.value)
