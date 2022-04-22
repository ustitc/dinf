package dinf.adapters

import dinf.domain.Dice
import dinf.domain.HashID
import dinf.domain.ID
import org.hashids.Hashids

class HashidsHashID(private val id: ID, private val hashids: Hashids) : HashID {

    constructor(dice: Dice, hashids: Hashids) : this(dice.id, hashids)

    override fun print(): String {
        return hashids.encode(id.number)
    }
}
