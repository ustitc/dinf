package dinf.domain

interface HashID {

    fun print(): String

    fun toID(): ID

    class Stub(private val hash: String = "", private val id: ID = ID.first()) : HashID {
        override fun print(): String = hash
        override fun toID(): ID = id
    }

}
