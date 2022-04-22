package dinf.domain

import dinf.types.NBString

interface HashID {

    fun print(): String

    class Simple(private val nbString: NBString) : HashID {

        constructor(string: String) : this(NBString(string))

        override fun print(): String {
            return nbString.toString()
        }
    }
}
