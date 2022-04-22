package dinf.domain

import dinf.types.NBString

interface ID {

    fun print(): String

    class Simple(private val nbString: NBString) : ID {

        constructor(string: String) : this(NBString(string))

        override fun print(): String {
            return nbString.toString()
        }
    }
}
