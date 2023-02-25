package dinf.app.html

interface HtmlContent {

    fun print(): String

    @JvmInline
    value class Simple(private val str: String): HtmlContent {

        constructor(int: Int) : this(int.toString())

        override fun print(): String = str
    }

}
