package dinf.adapters

import dinf.domain.Dice
import dinf.domain.ID
import dinf.domain.SerialNumber
import dinf.types.NBString
import org.hashids.Hashids

class HashID(private val value: Long, private val hashids: Hashids) : ID {

    constructor(dice: Dice, hashids: Hashids) : this(dice.serialNumber, hashids)

    constructor(serialNumber: SerialNumber, hashids: Hashids) : this(serialNumber.number, hashids)

    override fun print(): NBString {
        return NBString(hashids.encode(value))
    }
}
