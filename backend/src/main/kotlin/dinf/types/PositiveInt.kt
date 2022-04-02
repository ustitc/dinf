package dinf.types

import dev.ustits.krefty.core.Refined
import dev.ustits.krefty.dsl.refined
import dev.ustits.krefty.dsl.refinedOrNull
import dev.ustits.krefty.predicate.ints.Positive
import kotlin.jvm.JvmInline

@JvmInline
value class PositiveInt(private val refined: Refined<Positive, Int>) {

    constructor(int: Int) : this(int refined Positive())

    fun toInt(): Int {
        return refined.unrefined
    }

    operator fun minus(decrement: Int): Int {
        return toInt() - decrement
    }

}

typealias PInt = PositiveInt

fun Int.toPIntOrNull(): PInt? {
    return (this refinedOrNull Positive())?.let { PInt(this) }
}

fun Int.toPInt(): PInt {
    return toPIntOrNull()!!
}
