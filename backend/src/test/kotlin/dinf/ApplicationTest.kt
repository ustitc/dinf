package dinf

import dinf.config.Configuration
import dinf.domain.DiceDelete
import dinf.plugins.configureRouting
import dinf.domain.DiceSave
import dinf.domain.DiceSearch
import io.kotest.assertions.ktor.shouldHaveStatus
import io.kotest.core.spec.style.StringSpec
import io.ktor.http.*
import io.ktor.server.testing.*

class ApplicationTest : StringSpec({

    "root" {
        withTestApplication({
            configureRouting(
                Configuration(),
                Dependencies,
                DiceSave.Stub(),
                DiceSearch.Simple(),
                DiceDelete.Stub()
            )
        }) {
            handleRequest(HttpMethod.Get, "/").apply {
                response shouldHaveStatus HttpStatusCode.OK
            }
        }
    }

})
