package dinf.domain

import dinf.types.UserName

interface LoginedUser {

    suspend fun change(name: UserName)

    suspend fun deleteAccount()

    suspend fun toAuthor(): Author

}
