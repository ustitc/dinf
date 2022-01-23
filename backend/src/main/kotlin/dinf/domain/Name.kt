package dinf.domain

import dinf.types.NBString

interface Name {

    suspend fun change(new: NBString)

    val nbString: NBString

    class Stub(override var nbString: NBString) : Name {

        constructor(str: String) : this(NBString(str))

        override suspend fun change(new: NBString) {
            nbString = new
        }
    }

}
