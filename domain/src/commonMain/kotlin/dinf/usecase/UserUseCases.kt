package dinf.usecase

import dinf.types.AnonymousUser
import dinf.types.Credential
import dinf.types.RegisteredUser
import dinf.types.UserName

interface UserUseCases {

    fun AnonymousUser.login(credential: Credential): RegisteredUser

    fun RegisteredUser.changeName(name: UserName): RegisteredUser

    fun RegisteredUser.deleteAccount(): AnonymousUser

}
