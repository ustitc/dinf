package dinf.adapters

import dinf.createDiceEntity
import dinf.domain.SerialNumber
import dinf.test.sqLiteTestListener
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.flow.toList

class DBDicesTest : StringSpec({

    listeners(sqLiteTestListener)

    "lists all dices" {
        val count = 40
        repeat(count) {
            createDiceEntity()
        }

        val uc = DBDices()

        uc.flow().toList().size shouldBe count
    }

    "lists only specified dices" {
        val count = 40
        repeat(count) {
            createDiceEntity()
        }

        val uc = DBDices()

        val serialsCount = 10
        uc.dices(
            List(serialsCount) { SerialNumber.Simple(it + 1L) }
        ).size shouldBe serialsCount
    }

})
