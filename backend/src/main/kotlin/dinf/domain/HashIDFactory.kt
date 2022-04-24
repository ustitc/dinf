package dinf.domain

import dinf.types.PLong

interface HashIDFactory {

    fun fromStringOrNull(str: String): HashID?

    fun fromID(id: ID): HashID

    class Stub(private val hashID: HashID = HashID.Stub("stub", ID(PLong.fromLongOrNull(10)!!))) : HashIDFactory {
        override fun fromStringOrNull(str: String): HashID = hashID
        override fun fromID(id: ID): HashID = hashID
    }

}
