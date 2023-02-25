package dinf.domain

import dinf.types.NBString

@JvmInline
value class Name(private val nbString: NBString) {

    constructor(str: String) : this(NBString(str))

    fun print(): String {
        return nbString.toString()
    }

}
