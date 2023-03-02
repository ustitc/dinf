package dinf.app.adapters

import dinf.app.services.PublicIDFactory
import dinf.domain.ID
import dinf.app.services.PublicID
import dinf.types.toPLongOrNull
import org.hashids.Hashids

class HashidsPublicIDFactory(private val hashids: Hashids) : PublicIDFactory {

    private class HashidsID(private val hash: String, private val id: ID) : PublicID {
        override fun print(): String = hash
        override fun toID(): ID = id
    }

    override fun fromStringOrNull(str: String): PublicID? {
        return hashids.decodeHashidsIDOrNull(str)
    }

    override fun fromID(id: ID): PublicID {
        return hashids.encodeToHashidsID(id)
    }

    private fun Hashids.encodeToHashidsID(id: ID): HashidsID {
        val hash = encode(id.number.toLong())
        return HashidsID(hash, id)
    }

    private fun Hashids.decodeHashidsIDOrNull(str: String): HashidsID? {
        return decodeOneOrNull(str)?.toPLongOrNull()?.let {
            HashidsID(str, ID(it))
        }
    }
}
