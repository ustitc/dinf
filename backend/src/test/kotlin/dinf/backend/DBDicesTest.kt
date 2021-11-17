package dinf.backend

import dinf.exposed.postgresTestListeners
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import kotlinx.coroutines.flow.toList

class DBDicesTest : StringSpec({

    listeners(postgresTestListeners)

    "lists all dices" {
        val count = 40
        repeat(count) {
            createDiceEntity()
        }

        val uc = DBDices()

        uc.flow().toList().size shouldBe count
    }

    "finds dice" {
        val entity = createDiceEntity()
        val dices = DBDices()

        val foundDice = dices.dice(entity.id.value)

        foundDice shouldNotBe null
    }

    "doesn't find dice if it doesn't exist" {
        val dices = DBDices()

        val foundArticle = dices.dice(1)

        foundArticle shouldBe null
    }

})
