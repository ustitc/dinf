package dinf.adapters

import dinf.domain.Dice
import dinf.domain.HashID
import dinf.domain.SerialNumber
import org.hashids.Hashids

class HashidsHashID(private val value: Long, private val hashids: Hashids) : HashID {

    constructor(dice: Dice, hashids: Hashids) : this(dice.serialNumber, hashids)

    constructor(serialNumber: SerialNumber, hashids: Hashids) : this(serialNumber.number, hashids)

    override fun print(): String {
        return hashids.encode(value)
    }
}
