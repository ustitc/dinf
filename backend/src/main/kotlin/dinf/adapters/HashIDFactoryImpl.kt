package dinf.adapters

import dinf.domain.HashID
import dinf.domain.HashIDFactory
import dinf.domain.ID
import dinf.types.toPLongOrNull
import org.hashids.Hashids

class HashIDFactoryImpl(private val hashids: Hashids) : HashIDFactory {

    override fun fromStringOrNull(str: String): HashID? {
        return decodeOrNull(str)?.toPLongOrNull()?.let {
            object : HashID {
                override fun print(): String = str
                override fun toID(): ID = ID(it)
            }
        }
    }

    override fun fromID(id: ID): HashID {
        return object : HashID {
            override fun print(): String {
                return hashids.encode(id.number.toLong())
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
