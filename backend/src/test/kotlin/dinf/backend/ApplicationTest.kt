package dinf.backend

import dinf.backend.config.Configuration
import dinf.backend.plugins.configureRouting
import dinf.domain.DiceSave
import dinf.domain.DiceSearch
import dinf.domain.Dices
import io.kotest.assertions.ktor.shouldHaveStatus
import io.kotest.core.spec.style.StringSpec
import io.ktor.http.*
import io.ktor.server.testing.*

class ApplicationTest : StringSpec({

    "root" {
        withTestApplication({ configureRouting(Configuration(), Dices.Stub(), DiceSave.Stub(), DiceSearch.Stub()) }) {
            handleRequest(HttpMethod.Get, "/").apply {
                response shouldHaveStatus HttpStatusCode.OK
            }
        }
    }

})
