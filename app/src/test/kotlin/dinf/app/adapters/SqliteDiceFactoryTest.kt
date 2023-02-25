package dinf.app.adapters

import dinf.domain.Edges
import dinf.domain.Name
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class SqliteDiceFactoryTest : StringSpec({

    listeners(SqliteListener())

    "creates dice" {
        val factory = SqliteDiceFactory()
        val name = Name("test")
        val edges = Edges(listOf("1", "2", "3"))
        val userID = createUser()

        val dice = factory.create(name, edges, userID)

        dice.name.print() shouldBe name.print()
        dice.edges.toStringList() shouldBe edges.toStringList()
    }

    "creates dice without edges" {
        val factory = SqliteDiceFactory()
        val name = Name("test")
        val edges = Edges(listOf())
        val userID = createUser()

        val dice = factory.create(name, edges, userID)

        dice.edges.toStringList() shouldBe emptyList()
    }

})
