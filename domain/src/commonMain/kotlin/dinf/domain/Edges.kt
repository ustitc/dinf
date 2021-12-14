package dinf.domain

interface Edges {

    val stringList: List<String>

    suspend fun change(new: Edges)

    class Simple(override var stringList: List<String>) : Edges {

        constructor(vararg ints: Int) : this(ints.map { it.toString() })

        constructor(vararg strings: String) : this(strings.toList())

        override suspend fun change(new: Edges) {
            stringList = new.stringList
        }
    }

}
