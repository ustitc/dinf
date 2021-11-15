package dinf.domain

import dinf.types.NBString

interface Edge {

    suspend fun edit(new: NBString)

    val value: NBString

    class Stub(override var value: NBString) : Edge {

        constructor(str: String) : this(NBString(str))

        constructor(int: Int) : this(int.toString())

        override suspend fun edit(new: NBString) {
            value = new
        }

    }

}
