package dinf.domain

class Offset(private val page: Page, private val count: Count) {

    fun toInt() = (page - 1) * count.toInt()

}
