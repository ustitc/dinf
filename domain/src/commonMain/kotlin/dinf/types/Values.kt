package dinf.types

import kotlin.jvm.JvmInline

@JvmInline
value class Values constructor(val list: List<NBString>) {

    companion object {

        fun orThrow(vararg items: String): Values {
            return orThrow(items.toList())
        }

        fun orThrow(list: List<String>): Values {
            return Values(list.map { NBString.orNull(it)!! })
        }
    }
}
