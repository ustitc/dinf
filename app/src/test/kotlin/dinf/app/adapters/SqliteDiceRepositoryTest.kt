package dinf.app.adapters

import dinf.domain.Dice
import dinf.domain.ID
import dinf.domain.Name
import dinf.types.toPLongOrNull
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.flow.count
import kotlinx.coroutines.flow.toList

class SqliteDiceRepositoryTest : StringSpec({

    listeners(SqliteListener())

    "creates dice" {
        val repository = SqliteDiceRepository()
        val name = Name("test")
        val userID = createUser()

        val dice = repository.create(Dice.New(name = name, ownerId = userID, edges = emptyList()))

        dice.name.print() shouldBe name.print()
    }

    "lists all repository" {
        val count = 40
        repeat(count) {
            createDice()
        }

        val repository = SqliteDiceRepository()

        repository.flow().toList().size shouldBe count
    }

    "lists only specified repository" {
        val count = 40
        repeat(count) {
            createDice()
        }

        val repository = SqliteDiceRepository()

        val serialsCount = 10
        repository.list(
            List(serialsCount) {
                val number = it + 1L
                ID(number.toPLongOrNull()!!)
            }
        ).size shouldBe serialsCount
    }

    "dice is deleted" {
        val dice = createDice()
        val repository = SqliteDiceRepository()

        repository.remove(dice)

        SqliteDiceRepository().flow().count() shouldBe 0
    }

    "find dice without edges" {
        val dice = createDice(edges = emptyList())
        val repository = SqliteDiceRepository()

        val result = repository.oneOrNull(dice.id)

        result!!.edges shouldBe emptyList()
    }

    "dice is found by name" {
        createDice("pinky")
        val repository = SqliteDiceRepository()

        val result = repository.search("pinky")

        result shouldHaveSize 1
    }

    "dice is found by part of name" {
        createDice("pinky")
        val repository = SqliteDiceRepository()

        val result = repository.search("pi")

        result shouldHaveSize 1
    }

})
