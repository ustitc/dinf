package dinf.app.services

import dinf.app.auth.UserSession

data class DiceView(
    val id: PublicID,
    val name: String,
    val ownerId: PublicID,
    val edges: List<EdgeView>
) {

    fun belongsTo(session: UserSession): Boolean {
        return ownerId.toID().toLong() == session.id
    }

}