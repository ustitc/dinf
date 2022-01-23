package dinf.domain

import dinf.types.NBString

interface ID {

    fun print(): NBString

    class Simple(private val nbString: NBString) : ID {

        constructor(string: String) : this(NBString(string))

        override fun print(): NBString {
            return nbString
        }
    }
}
