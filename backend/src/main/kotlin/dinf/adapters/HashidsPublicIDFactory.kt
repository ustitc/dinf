package dinf.adapters

import dinf.domain.EditID
import dinf.domain.PublicIDFactory
import dinf.domain.ID
import dinf.domain.ShareID
import dinf.types.toPLongOrNull
import org.hashids.Hashids

class HashidsPublicIDFactory(
    private val shareHashids: Hashids,
    private val editHashids: Hashids,
) : PublicIDFactory {

    private class HashidsID(private val hash: String, private val id: ID) : ShareID, EditID {
        override fun print(): String = hash
        override fun toID(): ID = id
    }

    override fun shareIDFromStringOrNull(str: String): ShareID? {
        return shareHashids.decodeHashidsIDOrNull(str)
    }

    override fun editIDFromStringOrNull(str: String): EditID? {
        return editHashids.decodeHashidsIDOrNull(str)
    }

    override fun shareIDFromID(id: ID): ShareID {
        return shareHashids.encodeToHashidsID(id)
    }

    override fun editIDFromID(id: ID): EditID {
        return editHashids.encodeToHashidsID(id)
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
