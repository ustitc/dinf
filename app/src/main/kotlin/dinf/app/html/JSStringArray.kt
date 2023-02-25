package dinf.app.html

@JvmInline
value class JSStringArray(private val list: List<HtmlContent>): HtmlContent {

    override fun print(): String {
        return list
            .map { it.print() }
            .joinToString(prefix = "[", postfix = "]") { "\"$it\"" }
    }

}
