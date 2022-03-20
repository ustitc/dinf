package dinf.adapters

import dinf.domain.Name
import dinf.db.firstOrNull
import dinf.db.transaction
import dinf.domain.SerialNumber
import dinf.types.NBString
import dinf.types.toNBString

class DBName(private val diceSerial: SerialNumber) : Name {

    override val nbString: NBString
        get() = transaction {
            prepareStatement("SELECT name FROM dices WHERE id = ?")
                .also { it.setLong(1, diceSerial.number) }
                .use {
                    it.executeQuery().firstOrNull {
                        getString(1)
                    }!!.toNBString()
                }

        }

    override suspend fun change(new: NBString) {
        transaction {
            prepareStatement("UPDATE dices SET name = ? WHERE id = ?")
                .also {
                    it.setString(1, new.toString())
                    it.setLong(2, diceSerial.number)
                }.use { it.execute() }
        }
    }
}
