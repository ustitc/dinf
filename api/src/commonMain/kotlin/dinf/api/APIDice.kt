package dinf.api

import dinf.domain.Dice
import dinf.domain.Edges
import dinf.domain.Name
import dinf.domain.Roll
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
data class APIDice(
    override val id: Int,
    @SerialName("name") val nameField: String,
    @SerialName("edges") val edgesField: List<String>,
) : Dice {

    constructor(dice: Dice) : this(
        id = dice.id,
        nameField = dice.name.nbString.toString(),
        edgesField = dice.edges.stringList
    )

    override suspend fun roll(): Roll {
        return Dice.Simple(id, name, edges).roll()
    }

    @Transient
    override val name: Name<Dice> = Name.Stub(nameField)

    @Transient
    override val edges: Edges = Edges.Simple(edgesField)
}
