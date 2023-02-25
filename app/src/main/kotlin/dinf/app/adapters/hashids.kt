package dinf.app.adapters

import org.hashids.Hashids

fun Hashids.decodeOneOrNull(str: String): Long? {
    val array = decode(str)
    return if (array.isEmpty()) {
        null
    } else {
        array[0]
    }
}
