package dinf.adapters

import dinf.domain.Edges
import dinf.domain.Name
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class DBDiceFactoryTest : StringSpec({

    listeners(DBListener())

    "creates dice" {
        val factory = DBDiceFactory()
        val name = Name("test")
        val edges = Edges(listOf("1", "2", "3"))

        val dice = factory.create(name, edges)

        dice.name.print() shouldBe name.print()
        dice.edges.toStringList() shouldBe edges.toStringList()
    }

    "creates dice without edges" {
        val factory = DBDiceFactory()
        val name = Name("test")
        val edges = Edges(listOf())

        val dice = factory.create(name, edges)

        dice.edges.toStringList() shouldBe emptyList()
    }

})