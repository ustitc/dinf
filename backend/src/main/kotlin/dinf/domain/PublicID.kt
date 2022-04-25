package dinf.domain

interface PublicID {

    fun print(): String

    fun toID(): ID

    class Stub(private val hash: String = "", private val id: ID = ID.first()) : PublicID {
        override fun print(): String = hash
        override fun toID(): ID = id
    }

}
