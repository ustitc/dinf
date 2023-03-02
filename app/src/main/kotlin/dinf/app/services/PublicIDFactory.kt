package dinf.app.services

import dinf.domain.ID

interface PublicIDFactory {

    fun fromStringOrNull(str: String): PublicID?

    fun fromID(id: ID): PublicID

}
