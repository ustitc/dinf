package dinf.domain

import dinf.types.PLong

interface PublicIDFactory {

    fun fromStringOrNull(str: String): PublicID?

    fun fromID(id: ID): PublicID

    class Stub(
        private val publicID: PublicID = PublicID.Stub("stub", ID(PLong.fromLongOrNull(10)!!))
    ) : PublicIDFactory {
        override fun fromStringOrNull(str: String): PublicID = publicID
        override fun fromID(id: ID): PublicID = publicID
    }

}
