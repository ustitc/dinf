package dinf.domain

typealias Clicks = Long

interface Metric {

    suspend fun addClick()

    val clicks: Clicks

    data class Simple(override var clicks: Clicks) : Metric {

        override suspend fun addClick() {
            clicks += 1
        }
    }

    companion object {

        fun zero(): Metric {
            return Simple(0)
        }

    }
}
