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

@Serializable
@Resource("/oauth")
object OAuthResource {

    @Serializable
    @Resource("/google")
    data class Google(val oauth: OAuthResource = OAuthResource) {

        @Serializable
        @Resource("/google/callback")
        data class Callback(val oauth: OAuthResource = OAuthResource)

    }
}
