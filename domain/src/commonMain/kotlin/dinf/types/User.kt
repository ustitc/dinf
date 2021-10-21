package dinf.types

sealed class User

object AnonymousUser : User()

sealed class RegisteredUser(open val id: UserID, open val name: UserName) : User()

class SimpleUser(override val id: UserID, override val name: UserName) : RegisteredUser(id, name)

class AdminUser(override val id: UserID, override val name: UserName) : RegisteredUser(id, name)
