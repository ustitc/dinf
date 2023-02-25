package dinf.html

import io.ktor.util.*

@JvmInline
value class EscapedString(private val str: String): HtmlContent {

    override fun print(): String {
        return str.escapeHTML()
    }

}
