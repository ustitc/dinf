package dinf.domain

typealias Clicks = Long

interface Metric {

    suspend fun addClick()

    suspend fun clicks(): Clicks

    class Simple(private var clicks: Clicks) : Metric {

        override suspend fun addClick() {
            clicks += 1
        }

        override suspend fun clicks(): Clicks {
            return clicks
        }
    }
}
