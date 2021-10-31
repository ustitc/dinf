package dinf.types

import dev.ustits.krefty.core.Predicate
import dev.ustits.krefty.core.Refined
import dev.ustits.krefty.dsl.and
import dev.ustits.krefty.dsl.refined
import dev.ustits.krefty.predicate.collections.Each
import dev.ustits.krefty.predicate.collections.NotEmpty
import dev.ustits.krefty.predicate.string.NotBlank
import kotlin.jvm.JvmInline

@JvmInline
value class Values(private val refined: Refined<IsValues, List<String>>) {

    constructor(vararg items: String) : this(items.toList())

    constructor(list: List<String>) : this(list refined IsValues())

    fun stringArray(): Array<String> {
        return stringList().toTypedArray()
    }

    private fun stringList(): List<String> {
        return refined.unrefined
    }

}

class IsValues : Predicate<List<String>> by NotEmpty<List<String>>() and Each(NotBlank())
