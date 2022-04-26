package dinf.types

import dev.ustits.krefty.core.Refined
import dev.ustits.krefty.dsl.refined
import dev.ustits.krefty.dsl.refinedOrNull
import dev.ustits.krefty.predicate.string.NotBlank

class NotBlankString(private val refined: Refined<NotBlank, String>): CharSequence by refined.unrefined {

    constructor(str: String) : this(str refined NotBlank())

    override fun toString(): String = refined.unrefined

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as NotBlankString

        if (refined != other.refined) return false

        return true
    }

    override fun hashCode(): Int {
        return refined.hashCode()
    }

}

typealias NBString = NotBlankString

fun String.toNBStringOrNull(): NBString? {
    return (this refinedOrNull NotBlank())?.let { NBString(it) }
}
