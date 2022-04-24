package dinf.domain

import dinf.types.PLong

interface HashIDs {

    fun fromStringOrNull(str: String): HashID?

    fun fromID(id: ID): HashID

    class Stub(private val hashID: HashID = HashID.Stub("stub", ID(PLong.fromLongOrNull(10)!!))) : HashIDs {
        override fun fromStringOrNull(str: String): HashID = hashID
        override fun fromID(id: ID): HashID = hashID
    }

}
