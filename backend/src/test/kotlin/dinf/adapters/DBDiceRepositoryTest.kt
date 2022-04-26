package dinf.adapters

import dinf.domain.ID
import dinf.types.toPLongOrNull
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.flow.count
import kotlinx.coroutines.flow.toList

class DBDiceRepositoryTest : StringSpec({

    listeners(DBListener())

    "lists all repository" {
        val count = 40
        repeat(count) {
            createDiceEntity()
        }

        val repository = DBDiceRepository()

        repository.flow().toList().size shouldBe count
    }

    "lists only specified repository" {
        val count = 40
        repeat(count) {
            createDiceEntity()
        }

        val repository = DBDiceRepository()

        val serialsCount = 10
        repository.list(
            List(serialsCount) {
                val number = it + 1L
                ID(number.toPLongOrNull()!!)
            }
        ).size shouldBe serialsCount
    }

    "dice is deleted" {
        val dice = createDiceEntity()
        val repository = DBDiceRepository()

        repository.remove(dice)

        DBDiceRepository().flow().count() shouldBe 0
    }

    "find dice without edges" {
        val dice = createDiceEntity(edges = emptyList())
        val repository = DBDiceRepository()

        val result = repository.oneOrNull(dice.id)

        result!!.edges.toStringList() shouldBe emptyList()
    }

})
