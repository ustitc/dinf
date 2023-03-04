package dinf.app

import dinf.app.config.AppConfig
import dinf.app.plugins.configureAuth
import dinf.app.plugins.configureRouting
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
                configureRouting(config)
            }
            val response = client.get("/")
            response shouldHaveStatus HttpStatusCode.OK
        }
    }

})
