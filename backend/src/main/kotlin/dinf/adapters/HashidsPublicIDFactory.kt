package dinf.adapters

import dinf.domain.EditID
import dinf.domain.PublicIDFactory
import dinf.domain.ID
import dinf.domain.ShareID
import dinf.types.PLong
import dinf.types.toPLongOrNull
import org.hashids.Hashids

class HashidsPublicIDFactory(
    private val shareHashids: Hashids,
    private val editHashids: Hashids,
) : PublicIDFactory {

    override fun shareIDFromStringOrNull(str: String): ShareID? {
        return shareHashids.decodeOrNull(str)?.let {
            object : ShareID {
                override fun print(): String = str
                override fun toID(): ID = ID(it)
            }
        }
    }

    override fun editIDFromStringOrNull(str: String): EditID? {
        return editHashids.decodeOrNull(str)?.let {
            object : EditID {
                override fun print(): String = str
                override fun toID(): ID = ID(it)
            }
        }
    }

    override fun shareIDFromID(id: ID): ShareID {
        return object : ShareID {
            override fun print(): String {
                return shareHashids.encode(id.number)
            }

            override fun toID(): ID = id
        }
    }

    override fun editIDFromID(id: ID): EditID {
        return object : EditID {
            override fun print(): String {
                return shareHashids.encode(id.number)
            }

            override fun toID(): ID = id
        }
    }

    private fun Hashids.encode(pLong: PLong): String {
        return encode(pLong.toLong())
    }

    private fun Hashids.decodeOrNull(str: String): PLong? {
        val array = decode(str)
        return if (array.isEmpty()) {
            null
        } else {
            array[0]
        }?.toPLongOrNull()
    }
}
