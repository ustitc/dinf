package dinf

import dinf.config.Configuration
import dinf.plugins.configureAuth
import dinf.plugins.configureRouting
import io.kotest.assertions.ktor.client.shouldHaveStatus
import io.kotest.core.spec.style.StringSpec
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.server.testing.*

class ApplicationTest : StringSpec({

    "root" {
        val deps = AppDeps.Stub()
        testApplication {
            application {
                configureRouting(
                    Configuration(),
                    deps,
                )
                configureAuth(deps)
            }
            val response = client.get("/")
            response shouldHaveStatus HttpStatusCode.OK
        }
    }

})
