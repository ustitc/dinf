package dinf.domain

@JvmInline
value class Edges(private val list: List<String>) {

    constructor() : this(listOf())

    fun toStringList(): List<String> {
        return list
    }

}
