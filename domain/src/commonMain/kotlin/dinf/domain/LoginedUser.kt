package dinf.domain

import dinf.types.UserName

interface LoginedUser {

    fun change(name: UserName)

    fun deleteAccount()

    fun toAuthor(): Author

}
