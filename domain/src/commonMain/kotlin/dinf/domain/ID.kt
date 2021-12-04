package dinf.domain

import dinf.types.NBString

interface ID {

    val nbString: NBString

    class Simple(override val nbString: NBString) : ID {

        constructor(int: Int) : this(int.toString())

        constructor(string: String) : this(NBString(string))

    }

    class Empty : ID {

        override val nbString: NBString
            get() = throw IllegalStateException("Can't describe empty ID")
    }

}
