package dinf.backend

import dinf.exposed.postgresTestListeners
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
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

})
