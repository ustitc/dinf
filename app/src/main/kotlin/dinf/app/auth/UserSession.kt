package dinf.app.auth

import io.ktor.server.auth.*

data class UserSession(val id: Long, val name: String) : Principal
