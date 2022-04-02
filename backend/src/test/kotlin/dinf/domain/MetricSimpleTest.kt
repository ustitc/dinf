package dinf.domain

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class MetricSimpleTest : StringSpec({

    "increment will add one click" {
        val metric = Metric.Simple(10)

        metric.addClick()

        metric.clicks shouldBe 11L
    }

})
