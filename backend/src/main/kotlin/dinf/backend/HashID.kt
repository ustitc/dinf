package dinf.backend

import dinf.domain.ID
import dinf.domain.SerialNumber
import dinf.types.NBString
import org.hashids.Hashids

class HashID(private val value: Long, private val hashids: Hashids) : ID {

    constructor(serialNumber: SerialNumber, hashids: Hashids) : this(serialNumber.number, hashids)

    override fun print(): NBString {
        return NBString(hashids.encode(value))
    }
}
