package dinf

import dinf.config.Configuration
import dinf.config.Search
import dinf.plugins.configureRouting
import io.kotest.assertions.ktor.shouldHaveStatus
import io.kotest.core.spec.style.StringSpec
import io.ktor.http.*
import io.ktor.server.testing.*

class ApplicationTest : StringSpec({

    "root" {
        withTestApplication({
            configureRouting(
                Configuration(),
                AppDependencies(MeiliDependencies(Search())),
            )
        }) {
            handleRequest(HttpMethod.Get, "/").apply {
                response shouldHaveStatus HttpStatusCode.OK
            }
        }
    }

})
