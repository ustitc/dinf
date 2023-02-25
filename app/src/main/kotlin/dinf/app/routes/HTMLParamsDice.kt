package dinf.app.routes

import dinf.domain.Edges
import dinf.domain.Name
import dinf.types.toNBStringOrNull
import io.ktor.http.*

class HTMLParamsDice(val name: Name, val edges: Edges) {

    companion object {

        fun fromParametersOrNull(parameters: Parameters): HTMLParamsDice? {
            val name = parameters["name"]?.toNBStringOrNull()
            val edges = parameters.getAll("edges")?.filter { it.isNotBlank() } ?: emptyList()
            return if (name == null) {
                null
            } else {
                HTMLParamsDice(Name(name), Edges(edges))
            }
        }
    }

}
