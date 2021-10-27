package dinf.types

sealed class User

object AnonymousUser : User()

class RegisteredUser(val id: UserID, val name: UserName) : User()
