package dinf.domain

import dinf.types.toPLongOrNull
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.collections.shouldContainInOrder
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlin.random.Random

class DiceServiceTest : StringSpec({

    fun dice(name: String): Dice {
        return Dice(
            id = ID(Random.nextLong(1, 100).toPLongOrNull()!!),
            edges = mockk(),
            name = Name(name)
        )
    }

    "dices with higher click count must be first" {
        val first = dice("first")
        val second = dice("second")
        val third = dice("third")

        val metrics = DiceMetricRepository.InMemory(
            first to Metric.Simple(10),
            second to Metric.Simple(3),
            third to Metric.Simple(0)
        )

        val diceRepository: DiceRepository = mockk()
        coEvery { diceRepository.list(any()) } returns listOf(first, second, third)

        val service = DiceService(
            diceFactory = mockk(),
            diceRepository = diceRepository,
            searchIndexRepository = SearchIndexRepository.Stub(),
            publicIDFactory = mockk(),
            diceMetricRepository = metrics,
            diceOwnerFactory = mockk()
        )

        service.search(SearchQuery("any")) shouldContainInOrder listOf(first, second, third)

        coVerify { diceRepository.list(any()) }
    }

})
