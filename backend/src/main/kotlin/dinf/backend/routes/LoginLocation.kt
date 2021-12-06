package dinf.backend.routes

import io.ktor.locations.*

@Location("/login")
object LoginLocation {

    @Location("")
    data class Form(val root: LoginLocation = LoginLocation, val fail: Boolean = false)

}
