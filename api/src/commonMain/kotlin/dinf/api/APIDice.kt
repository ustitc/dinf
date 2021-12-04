package dinf.api

import dinf.domain.Dice
import dinf.domain.Edges
import dinf.domain.ID
import dinf.domain.Name
import dinf.domain.Roll
import dinf.types.NBString
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
data class APIDice(
    @SerialName("id") val idField: String,
    @SerialName("name") val nameField: String,
    @SerialName("edges") val edgesField: List<String>,
) : Dice {

    constructor(dice: Dice) : this(
        idField = dice.id.nbString.toString(),
        nameField = dice.name.nbString.toString(),
        edgesField = dice.edges.stringList
    )

    override suspend fun roll(): Roll {
        return Dice.Simple(id, name, edges).roll()
    }

    @Transient
    override val id: ID = ID.Simple(NBString(idField))

    @Transient
    override val name: Name<Dice> = APIDiceName(NBString(nameField))

    @Transient
    override val edges: Edges = Edges.Simple(edgesField)
}
