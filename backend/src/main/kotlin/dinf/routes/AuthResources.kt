package dinf.routes

import io.ktor.resources.*
import kotlinx.serialization.Serializable

@Serializable
@Resource("/login")
class LoginResource(val failed: Boolean? = null)

@Serializable
@Resource("/register")
class RegisterResource(val userExists: Boolean? = null)

@Serializable
@Resource("/logout")
object LogoutResource
