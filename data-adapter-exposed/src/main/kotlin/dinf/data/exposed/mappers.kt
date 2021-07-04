package dinf.data.exposed

import dinf.data.PermissionType
import dinf.types.NotBlankString
import dinf.types.UserName

fun PermissionType.toSqlString(): String? = when(this) {
    PermissionType.ADMIN -> this.toString().lowercase()
    else -> null
}

fun String?.toPermissionType() = when(this) {
    null -> PermissionType.SIMPLE
    else -> PermissionType.valueOf(this.uppercase())
}

fun String.toUserName(): UserName = UserName(
    NotBlankString.orNull(this)!!
)
