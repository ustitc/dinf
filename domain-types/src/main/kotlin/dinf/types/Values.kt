package dinf.types

import arrow.refinement.Refined
import arrow.refinement.ensure

@JvmInline
value class Values private constructor(val list: List<NotBlankString>) {
    companion object : Refined<List<NotBlankString>, Values>(::Values, {
        ensure((it.isNotEmpty() to "$it should not be empty"))
    })
}
