package dinf.adapters

import dinf.domain.Dice
import dinf.domain.HashID
import dinf.domain.ID
import org.hashids.Hashids

class HashidsHashID(private val value: Long, private val hashids: Hashids) : HashID {

    constructor(dice: Dice, hashids: Hashids) : this(dice.id, hashids)

    constructor(id: ID, hashids: Hashids) : this(id.number, hashids)

    override fun print(): String {
        return hashids.encode(value)
    }
}
