package dinf.domain

import dinf.types.Credential

interface AnonymousUser {

    fun toLogined(credential: Credential): LoginedUser

}
