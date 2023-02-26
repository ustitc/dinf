package dinf.app.adapters

import dinf.domain.Edge
import dinf.domain.ID
import dinf.domain.Name
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class SqliteDiceFactoryTest : StringSpec({

    listeners(SqliteListener())

    "creates dice" {
        val factory = SqliteDiceFactory()
        val name = Name("test")
        val edges = listOf(
            Edge(ID.first(), "1"),
            Edge(ID.first(), "2"),
            Edge(ID.first(), "3")
        )
        val userID = createUser()

        val dice = factory.create(name, edges, userID)

        dice.name.print() shouldBe name.print()
        dice.edges.asEdgeList() shouldBe edges
    }

    "creates dice without edges" {
        val factory = SqliteDiceFactory()
        val name = Name("test")
        val edges = emptyList<Edge>()
        val userID = createUser()

        val dice = factory.create(name, edges, userID)

        dice.edges.asEdgeList() shouldBe emptyList()
    }

})
