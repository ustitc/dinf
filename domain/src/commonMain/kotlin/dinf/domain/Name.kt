package dinf.domain

import dinf.types.NBString

interface Name<T> {

    suspend fun change(new: NBString)

    val nbString: NBString

    class Stub<T>(override var nbString: NBString) : Name<T> {

        constructor(str: String) : this(NBString(str))

        override suspend fun change(new: NBString) {
            nbString = new
        }
    }

}
