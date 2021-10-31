package dinf.types

import dev.ustits.krefty.core.Predicate
import dev.ustits.krefty.core.Refined
import dev.ustits.krefty.dsl.refined
import dev.ustits.krefty.predicate.string.NotBlank
import kotlin.jvm.JvmInline

@JvmInline
value class UserName(private val refined: Refined<IsUserName, String>) {

    constructor(str: String) : this(str refined IsUserName())

    override fun toString(): String = refined.unrefined

}

class IsUserName : Predicate<String> by NotBlank()
