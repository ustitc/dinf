package dinf.html

@JvmInline
value class HTMLTextWithNewLines(private val text: String) : HtmlContent {

    constructor(htmlContent: HtmlContent) : this(htmlContent.print())

    override fun print(): String {
        return text.lines().joinToString(separator = "&#13;&#10;")
    }
}
