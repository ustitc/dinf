package dinf.adapters

import dinf.domain.HashID
import dinf.domain.HashIDs
import dinf.domain.ID
import org.hashids.Hashids

class HashIDsImpl(private val hashids: Hashids) : HashIDs {

    override fun fromStringOrNull(str: String): HashID? {
        return decodeOrNull(str)?.let {
            object : HashID {
                override fun print(): String = str
                override fun toID(): ID = ID(it)
            }
        }
    }

    override fun fromID(id: ID): HashID {
        return object : HashID {
            override fun print(): String {
                return hashids.encode(id.number)
            }

            override fun toID(): ID = id
        }
    }

    private fun decodeOrNull(str: String): Long? {
        val array = hashids.decode(str)
        return if (array.isEmpty()) {
            null
        } else {
            array[0]
        }
    }
}
