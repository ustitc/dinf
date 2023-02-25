package dinf.app.adapters

import dinf.domain.ID
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe

class SqliteDiceOwnerTest : StringSpec({

    listeners(SqliteListener())

    "find the dice" {
        val userId = createUser()
        val diceId = createDice(ownerID = userId).id
        val diceOwner = SqliteDiceOwner(userId)

        val dice = diceOwner.findDice(diceId)

        dice shouldNotBe null
    }

    "find the dice with no edges" {
        val userId = createUser()
        val diceId = createDice(ownerID = userId, edges = emptyList()).id
        val diceOwner = SqliteDiceOwner(userId)

        val dice = diceOwner.findDice(diceId)

        dice shouldNotBe null
    }

    "deletes links and the dice" {
        val userId = createUser()
        val diceId = createDice(ownerID = userId).id
        val diceOwner = SqliteDiceOwner(userId)

        diceOwner.deleteDice(diceId)

        diceOwner.findDice(diceId) shouldBe null
        SqliteDiceRepository().oneOrNull(diceId) shouldBe null
    }

    "doesn't delete dice if the userId is not the owner" {
        val userId = createUser()
        val diceId = createDice(ownerID = userId).id
        val diceOwner = SqliteDiceOwner(ID.fromLong(351L))

        diceOwner.deleteDice(diceId)

        SqliteDiceOwner(userId).findDice(diceId) shouldNotBe null
        SqliteDiceRepository().oneOrNull(diceId) shouldNotBe null
    }

})
