package dinf.domain

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.collections.shouldContainInOrder
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import kotlinx.coroutines.flow.toList

class DiceMetricRepositoryInMemoryTest : StringSpec({

    val metrics = DiceMetricRepository.InMemory()

    beforeTest {
        metrics.clear()
    }

    "no metric for unknown id" {
        val metric = metrics.forID(ID.first())

        metric shouldBe null
    }

    "return metric for existing id" {
        val id = ID.first()
        metrics.create(id, Metric.zero())

        val metric = metrics.forID(id)

        metric shouldNotBe null
    }

    "return zero metric for unknown id" {
        val id = ID.first()

        val metric = metrics.forIDOrZero(id)

        metric.clicks shouldBe 0L
    }

    "no duplicates" {
        val id = ID.first()
        metrics.create(id, Metric.zero())
        metrics.create(id, Metric.zero())

        val all = metrics.popularIDs().toList()

        all shouldHaveSize 1
    }

    "serials with highest metric first" {
        val first = ID.fromLong(1)
        val second = ID.fromLong(2)
        val third = ID.fromLong(3)

        metrics.create(first, Metric.Simple(2))
        metrics.create(second, Metric.Simple(10))
        metrics.create(third, Metric.Simple(4))

        metrics.popularIDs().toList().shouldContainInOrder(
            second,
            third,
            first
        )
    }

    "metric deletes" {
        val id = ID.first()
        metrics.create(id, Metric.zero())

        metrics.removeForID(id)

        metrics.popularIDs().toList() shouldHaveSize 0
    }

    "removing unknown metric doesn't cause and error" {
        metrics.removeForID(ID.first())

        metrics.popularIDs().toList() shouldHaveSize 0
    }

})
