package dinf

import dinf.config.AppConfig
import dinf.plugins.configureAuth
import dinf.plugins.configureRouting
import io.kotest.assertions.ktor.client.shouldHaveStatus
import io.kotest.core.spec.style.StringSpec
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.resources.*
import io.ktor.server.testing.*
import io.mockk.mockk

class ApplicationTest : StringSpec({

    "root" {
        deps = AppDepsMock()
        val config = AppConfig()
        testApplication {
            application {
                install(Resources)
                configureAuth(config, mockk())
                configureRouting(AppConfig())
            }
            val response = client.get("/")
            response shouldHaveStatus HttpStatusCode.OK
        }
    }

})
