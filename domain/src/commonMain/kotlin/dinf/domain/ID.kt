package dinf.domain

import dinf.types.NBString

interface ID {

    suspend fun print(): NBString

    class Simple(private val nbString: NBString) : ID {

        constructor(string: String) : this(NBString(string))

        override suspend fun print(): NBString {
            return nbString
        }
    }

    class Serial(val int: Int) : ID by Simple(int.toString())

    class Empty : ID {

        override suspend fun print(): NBString {
            throw IllegalStateException("Can't print empty ID")
        }
    }

}
