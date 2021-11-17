package dinf.domain

interface Edges {

    val stringList: List<String>

    class Simple(override val stringList: List<String>) : Edges {

        constructor(vararg ints: Int) : this(ints.map { it.toString() })

        constructor(vararg strings: String) : this(strings.toList())

    }

}
