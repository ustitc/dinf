package dinf.data.exposed

import dinf.types.NotBlankString
import dinf.types.UserName

fun String.toUserName(): UserName = UserName(
    NotBlankString.orNull(this)!!
)
