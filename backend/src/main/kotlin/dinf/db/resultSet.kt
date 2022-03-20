package dinf.db

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
