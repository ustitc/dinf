package dinf.domain

import dinf.types.Credential

interface AnonymousUser {

    suspend fun toLogined(credential: Credential): LoginedUser

}
