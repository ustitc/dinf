package dinf.domain

interface PublicIDFactory {

    fun fromStringOrNull(str: String): PublicID?

    fun fromID(id: ID): PublicID

}
