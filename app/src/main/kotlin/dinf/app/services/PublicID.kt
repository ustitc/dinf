package dinf.app.services

import dinf.domain.ID

interface PublicID {

    fun print(): String

    fun toID(): ID

}
