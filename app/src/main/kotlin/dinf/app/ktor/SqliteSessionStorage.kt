package dinf.app.ktor

import dinf.app.db.firstOrNull
import dinf.app.db.sql
import io.ktor.server.sessions.*

class SqliteSessionStorage : SessionStorage {

    override suspend fun invalidate(id: String) {
        sql("DELETE FROM sessions WHERE id = ?") {
            setString(1, id)
            execute()
        }
    }

    override suspend fun read(id: String): String {
        return sql("SELECT value FROM sessions WHERE id = ?") {
            setString(1, id)
            executeQuery().firstOrNull {
                getString("value")
            }
        } ?: throw NoSuchElementException("Session $id not found")
    }

    override suspend fun write(id: String, value: String) {
        sql("INSERT OR REPLACE INTO sessions (id, value) VALUES (?, ?)") {
            setString(1, id)
            setString(2, value)
            execute()
        }
    }
}
