package dinf.types

import dev.ustits.krefty.core.Refined
import dev.ustits.krefty.dsl.refined
import dev.ustits.krefty.predicate.ints.Positive
import kotlin.jvm.JvmInline

@JvmInline
value class PositiveInt(private val refined: Refined<Positive, Int>) {

    constructor(int: Int) : this(int refined Positive())

    fun toInt(): Int {
        return refined.unrefined
    }

}

typealias PInt = PositiveInt
