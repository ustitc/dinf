package dinf.domain

typealias Clicks = Long

interface Metric {

    suspend fun increment()

    suspend fun clicks(): Clicks

    class Stub(private val clicks: Clicks) : Metric {

        override suspend fun increment() {
            error("Can't increment stub metric")
        }

        override suspend fun clicks(): Clicks {
            return clicks
        }
    }
}
