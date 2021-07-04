package dinf.usecase

import arrow.core.Either
import dinf.types.*

interface UserUseCases {

    fun AnonymousUser.login(credential: Credential): RegisteredUser

    fun RegisteredUser.changeName(name: UserName): RegisteredUser

    fun RegisteredUser.deleteAccount(): AnonymousUser

    fun AdminUser.promoteToAdmin(user: SimpleUser): Either<UserNotFoundError, AdminUser>

    fun AdminUser.deleteAccount(user: SimpleUser)

}
