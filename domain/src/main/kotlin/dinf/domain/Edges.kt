package dinf.domain

interface Edges {

    fun asEdgeList(): List<Edge>

    fun toStringList(): List<String> {
        return asEdgeList().map { it.value }
    }

    fun replaceAll(list: List<Edge>)

}
