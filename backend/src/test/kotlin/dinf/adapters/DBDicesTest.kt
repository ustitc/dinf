package dinf.adapters

import dinf.domain.ID
import dinf.types.toPLongOrNull
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.flow.toList

class DBDicesTest : StringSpec({

    listeners(DBListener())

    "lists all dices" {
        val count = 40
        repeat(count) {
            createDiceEntity()
        }

        val dices = DBDices()

        dices.flow().toList().size shouldBe count
    }

    "lists only specified dices" {
        val count = 40
        repeat(count) {
            createDiceEntity()
        }

        val dices = DBDices()

        val serialsCount = 10
        dices.list(
            List(serialsCount) {
                val number = it + 1L
                ID(number.toPLongOrNull()!!)
            }
        ).size shouldBe serialsCount
    }

})
