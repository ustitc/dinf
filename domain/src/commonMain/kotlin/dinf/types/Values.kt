package dinf.types

import kotlin.jvm.JvmInline

@JvmInline
value class Values(private val list: List<NBString>) {

    constructor(vararg items: String) : this(items.toList().map { NBString(it) })

    fun stringArray(): Array<String> {
        return stringList().toTypedArray()
    }

    private fun stringList(): List<String> {
        return list.map { it.toString() }
    }

}
