package dinf.adapters

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.flow.count

class DBDiceDeleteTest : StringSpec({

    listeners(DBListener())

    "dice is deleted" {
        val dice = createDiceEntity()
        val deleteDice = DBDiceDelete()

        deleteDice.invoke(dice)

        DBDiceRepository().flow().count() shouldBe 0
    }

})
