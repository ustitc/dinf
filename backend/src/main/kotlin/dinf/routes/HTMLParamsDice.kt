package dinf.routes

import dinf.domain.Dice
import dinf.domain.Edges
import dinf.types.NBString
import dinf.types.toNBStringOrNull
import io.ktor.http.*

class HTMLParamsDice(name: NBString, edges: List<String>) : Dice by Dice.New(name, Edges.Simple(edges)) {

    companion object {

        fun fromParametersOrNull(parameters: Parameters): HTMLParamsDice? {
            val name = parameters["name"]?.toNBStringOrNull()
            val edges = parameters.getAll("edges")?.filter { it.isNotBlank() }
            return if (name == null || edges == null) {
                null
            } else {
                HTMLParamsDice(name, edges)
            }
        }
    }

}
