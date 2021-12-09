package dinf.domain

interface SerialNumber {

    val number: Long

    class Simple(override val number: Long) : SerialNumber

    class Empty : SerialNumber {
        override val number: Long
            get() = throw IllegalStateException("Can't compute number for empty serial")
    }

}
