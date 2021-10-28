package dinf.domain

import dinf.types.NBString
import dinf.types.Values

data class Content(
    var title: NBString,
    var description: String,
    var values: Values
)
