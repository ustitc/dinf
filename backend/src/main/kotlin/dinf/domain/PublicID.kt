package dinf.domain

interface PublicID {

    fun print(): String

    fun toID(): ID

    class Stub(private val str: String = "", private val id: ID = ID.first()) : PublicID {
        override fun print(): String = str
        override fun toID(): ID = id
    }

}
