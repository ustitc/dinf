package dinf.domain

import dinf.types.Credential
import dinf.types.UserName

interface User {

    fun login(credential: Credential)

    fun change(name: UserName)

    fun deleteAccount()

}
