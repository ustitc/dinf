package dinf.data.exposed

import dinf.data.PermissionEntity
import dinf.data.PermissionRepository
import dinf.types.UserID

class ExposedPermissionRepository : PermissionRepository {

    override fun findByUserID(userID: UserID): PermissionEntity? {
        TODO("Not yet implemented")
    }

    override fun saveOrUpdate(entity: PermissionEntity) {
        TODO("Not yet implemented")
    }
}
