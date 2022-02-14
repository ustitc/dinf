package dinf.config

import org.hashids.Hashids

data class URL(
    val salt: String,
    val length: Int
) {

    fun hashids(): Hashids {
        return Hashids(salt, length)
    }

}
