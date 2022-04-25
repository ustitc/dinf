package dinf.domain

interface PublicIDFactory {

    fun shareIDFromStringOrNull(str: String): ShareID?

    fun editIDFromStringOrNull(str: String): EditID?

    fun shareIDFromID(id: ID): ShareID

    fun editIDFromID(id: ID): EditID

    class Stub(
        private val shareID: ShareID? = null,
        private val editID: EditID? = null,
    ) : PublicIDFactory {
        override fun shareIDFromStringOrNull(str: String): ShareID? = shareID
        override fun editIDFromStringOrNull(str: String): EditID? = editID
        override fun shareIDFromID(id: ID): ShareID = shareID!!
        override fun editIDFromID(id: ID): EditID = editID!!
    }

}
