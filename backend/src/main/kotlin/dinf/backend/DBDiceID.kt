package dinf.backend

import dinf.domain.ID

class DBDiceID(private val int: Int) : ID by ID.Simple(int.toString())
