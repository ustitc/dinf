package dinf.types

import kotlin.jvm.JvmInline

@JvmInline
value class Values constructor(val list: List<NBString>) {

    constructor(vararg items: String) : this(items.toList().map { NBString(it) })

}
