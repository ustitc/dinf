package dinf.api

import dinf.domain.Dice
import dinf.domain.Name
import dinf.types.NBString

class APIDiceName(override val nbString: NBString) : Name<Dice> {

    override suspend fun change(new: NBString) {
        TODO("Not yet implemented")
    }

}