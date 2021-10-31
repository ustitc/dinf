package dinf.types

import dev.ustits.krefty.core.Refined
import dev.ustits.krefty.dsl.refined
import dev.ustits.krefty.predicate.string.NotBlank
import kotlin.jvm.JvmInline

@JvmInline
value class NotBlankString(private val refined: Refined<NotBlank, String>) {

    constructor(str: String) : this(str refined NotBlank())

    override fun toString(): String = refined.unrefined

}

typealias NBString = NotBlankString
