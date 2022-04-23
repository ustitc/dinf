package dinf.domain

interface HashIDs {

    fun fromStringOrNull(str: String): HashID?

    fun fromID(id: ID): HashID

    class Stub(private val hashID: HashID = HashID.Stub("stub", ID(1))) : HashIDs {
        override fun fromStringOrNull(str: String): HashID = hashID
        override fun fromID(id: ID): HashID = hashID
    }

}
