package dinf.domain

class SearchQuery(val text: String, private val page: Page, private val count: Count) {

    constructor(text: String) : this(text, Page(1), Count(20))

    val offset: Int
        get() {
            return (page.toInt() - 1) * count.toInt()
        }

    val limit: Int
        get() {
            return count.toInt()
        }

    override fun toString(): String {
        return "SearchQuery(text='$text', offset=$offset, limit=$limit)"
    }

}
