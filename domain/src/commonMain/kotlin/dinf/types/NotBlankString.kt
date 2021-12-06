package dinf.types

import dev.ustits.krefty.core.Refined
import dev.ustits.krefty.dsl.refined
import dev.ustits.krefty.dsl.refinedOrNull
import dev.ustits.krefty.predicate.string.NotBlank

class NotBlankString(private val refined: Refined<NotBlank, String>): CharSequence by refined.unrefined {

    constructor(str: String) : this(str refined NotBlank())

    override fun toString(): String = refined.unrefined

}

typealias NBString = NotBlankString

fun String.toNBStringOrNull(): NBString? {
    return (this refinedOrNull NotBlank())?.let { NBString(it) }
}
