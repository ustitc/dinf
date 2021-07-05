package dinf.types

import arrow.refinement.numbers.PositiveInt

@JvmInline
value class ArticleID(private val value: PositiveInt) {

    fun toInt(): Int = value.value

    override fun toString(): String {
        return "ArticleID(value=${toInt()})"
    }

}
