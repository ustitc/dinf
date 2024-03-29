package dinf.app.db

import dinf.types.PLong
import dinf.types.toPLongOrNull
import java.sql.PreparedStatement
import java.sql.ResultSet

fun <R> ResultSet.toSequence(block: ResultSet.() -> R): Sequence<R> {
    return sequence {
        while (this@toSequence.next()) {
            yield(block(this@toSequence))
        }
        close()
    }
}

fun ResultSet.forEach(block: (ResultSet) -> Unit) {
    return toSequence { this }.forEach { block(it) }
}

fun <R> ResultSet.firstOrNull(block: ResultSet.() -> R): R? {
    return toSequence(block).firstOrNull()
}

fun <R> ResultSet.first(block: ResultSet.() -> R): R {
    return firstOrNull(block)!!
}

fun ResultSet.getPLongOrNull(name: String): PLong? {
    return getLong(name).toPLongOrNull()
}

fun ResultSet.getPLong(name: String): PLong {
    return getPLongOrNull(name)!!
}

fun PreparedStatement.setPLong(index: Int, x: PLong) {
    setLong(index, x.toLong())
}
