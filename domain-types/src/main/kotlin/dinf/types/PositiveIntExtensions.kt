package dinf.types

import arrow.refinement.numbers.PositiveInt

fun Int.toPositiveInt(): PositiveInt? = PositiveInt.orNull(this)
