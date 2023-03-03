package dinf.app.adapters

import dinf.domain.Edge
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class SqliteEdgeRepositoryTest : StringSpec({

    listeners(SqliteListener())

    "update edge" {
        val dice = createDice()
        val repository = SqliteEdgeRepository()
        val created = repository.create(Edge.New(value = "", diceId = dice.id))

        repository.update(created.copy(value = "test"))

        repository.oneOrNull(created.id)?.value shouldBe "test"
    }

})
