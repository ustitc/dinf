package dinf.htmx

import kotlinx.html.HEAD
import kotlinx.html.meta
import org.json.simple.JSONObject
import java.time.Duration

data class HTMXConfiguration(
    val timeout: Duration
) {

    fun jsonString(): String {
        return JSONObject().also {
            it["timeout"] = timeout.toMillis()
        }.toJSONString()
    }
}

private const val META_NAME = "htmx-config"

fun HEAD.htmxConfiguration(htmxConfiguration: HTMXConfiguration) {
    meta {
        name = META_NAME
        content = htmxConfiguration.jsonString()
    }
}
