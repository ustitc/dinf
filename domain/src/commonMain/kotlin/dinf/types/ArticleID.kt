package dinf.types

import kotlin.jvm.JvmInline

@JvmInline
value class ArticleID(private val value: PositiveInt) {

    fun toInt(): Int = value.value

    override fun toString(): String {
        return "ArticleID(value=${toInt()})"
    }

}
