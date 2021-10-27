package dinf.domain

import dinf.types.Credential
import dinf.types.UserID
import dinf.types.UserName

interface User {

    val id: UserID

    fun login(credential: Credential)

    fun changeName(name: UserName)

    fun deleteAccount()

}
