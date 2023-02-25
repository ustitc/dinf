package dinf.domain

data class SearchQuery(
    val text: String,
    val page: Page = Page(1),
    val count: Count = Count(20)
)
