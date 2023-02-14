package dinf.domain

interface PublicIDFactory {

    fun fromStringOrNull(str: String): PublicID?

    fun fromID(id: ID): PublicID

    class Stub(
        private val publicID: PublicID? = null,
    ) : PublicIDFactory {
        override fun fromStringOrNull(str: String): PublicID? = publicID
        override fun fromID(id: ID): PublicID = publicID!!
    }

}
