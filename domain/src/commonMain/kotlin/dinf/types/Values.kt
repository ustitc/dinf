package dinf.types

import kotlin.jvm.JvmInline

@JvmInline
value class Values private constructor(val list: List<NotBlankString>)
