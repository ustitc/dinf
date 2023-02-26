package dinf.domain

import dinf.types.toPLongOrNull
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.collections.shouldContainInOrder
import io.mockk.mockk
import kotlin.random.Random

class DiceServiceTest : StringSpec({

    class Stub(name: String = "stub") : Dice by Dice.Simple(
        id = ID(Random.nextLong(1, 100).toPLongOrNull()!!),
        name = Name(name),
        edges = mockk()
    )

    "dices with higher click count must be first" {
        val first = Stub("first")
        val second = Stub("second")
        val third = Stub("third")

        val metrics = DiceMetricRepository.InMemory(
            first to Metric.Simple(10),
            second to Metric.Simple(3),
            third to Metric.Simple(0)
        )

        val service = DiceService.Impl(
            diceFactory = mockk(),
            diceRepository = DiceRepository.Stub(listOf(first, second, third).toMutableList()),
            searchIndexRepository = SearchIndexRepository.Stub(listOf(first.id, second.id, third.id)),
            publicIDFactory = mockk(),
            diceMetricRepository = metrics,
            diceOwnerFactory = mockk()
        )

        service.search(SearchQuery("any")) shouldContainInOrder listOf(first, second, third)
    }



})
