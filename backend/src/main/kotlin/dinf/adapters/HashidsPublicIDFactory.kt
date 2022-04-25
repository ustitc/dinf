package dinf.adapters

import dinf.domain.PublicID
import dinf.domain.PublicIDFactory
import dinf.domain.ID
import dinf.types.toPLongOrNull
import org.hashids.Hashids

class HashidsPublicIDFactory(private val hashids: Hashids) : PublicIDFactory {

    override fun fromStringOrNull(str: String): PublicID? {
        return decodeOrNull(str)?.toPLongOrNull()?.let {
            object : PublicID {
                override fun print(): String = str
                override fun toID(): ID = ID(it)
            }
        }
    }

    override fun fromID(id: ID): PublicID {
        return object : PublicID {
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
