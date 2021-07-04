package dinf.types

import arrow.refinement.Refined
import arrow.refinement.ensure

@JvmInline
value class NotBlankString private constructor(private val value: String) {

    override fun toString(): String = value

    companion object : Refined<String, NotBlankString>(::NotBlankString, {
        ensure((it.isNotBlank() to "string should be not blank"))
    })

}
