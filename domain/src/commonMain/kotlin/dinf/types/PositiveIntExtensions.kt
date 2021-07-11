package dinf.types

fun Int.toPositiveInt(): PositiveInt? = PositiveInt.orNull(this)
