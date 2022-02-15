package dev.ustits.htmx

import kotlinx.html.HEAD
import kotlinx.html.meta
import java.time.Duration

data class HTMXConfiguration(
    val timeout: Duration
) {

    fun jsonString(): String {
        return """
            {
                "timeout": ${timeout.toMillis()}
            }
        """.trimIndent()
    }
}

private const val META_NAME = "htmx-config"

fun HEAD.htmxConfiguration(htmxConfiguration: HTMXConfiguration) {
    meta {
        name = META_NAME
        content = htmxConfiguration.jsonString()
    }
}
