package dinf.types

import dev.ustits.krefty.core.Predicate
import dev.ustits.krefty.core.Refined
import dev.ustits.krefty.dsl.refined
import dev.ustits.krefty.predicate.ints.Positive
import kotlin.jvm.JvmInline

@JvmInline
value class ArticleID(private val refined: Refined<IsArticleID, Int>) {

    constructor(int: Int) : this(int refined IsArticleID())

    fun toInt(): Int = refined.unrefined

    override fun toString(): String {
        return "ArticleID(value=${toInt()})"
    }

}

class IsArticleID : Predicate<Int> by Positive()
