package dinf.backend

import dinf.domain.SerialNumber
import org.hashids.Hashids

class HashSerialNumber(private val hash: String, private val hashids: Hashids) : SerialNumber {

    override val number: Long by lazy { hashids.decode(hash)[0] }
}