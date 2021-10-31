package dinf.types

import kotlin.jvm.JvmInline

@JvmInline
value class ArticleID(private val value: PositiveInt) {

    constructor(int: Int) : this(PInt(int))

    fun toInt(): Int = value.toInt()

    override fun toString(): String {
        return "ArticleID(value=${toInt()})"
    }

}
