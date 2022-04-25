package dinf.domain

sealed interface PublicID {

    fun print(): String

    fun toID(): ID

}

interface ShareID : PublicID {
    class Stub(private val str: String = "", private val id: ID = ID.first()) : ShareID {
        override fun print(): String = str
        override fun toID(): ID = id
    }
}

interface EditID: PublicID {
    class Stub(private val str: String = "", private val id: ID = ID.first()) : EditID {
        override fun print(): String = str
        override fun toID(): ID = id
    }
}
