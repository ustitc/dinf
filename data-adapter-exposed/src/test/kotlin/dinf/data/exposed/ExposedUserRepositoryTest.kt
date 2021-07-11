package dinf.data.exposed

import dinf.data.PermissionType
import dinf.data.UserEditEntity
import dinf.data.UserEntity
import dinf.data.UserSaveEntity
import dinf.types.NotBlankString
import dinf.types.UserID
import dinf.types.UserName
import io.kotest.assertions.arrow.either.beLeft
import io.kotest.assertions.arrow.either.shouldBeRight
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import kotlinx.datetime.toKotlinInstant
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.Instant

class ExposedUserRepositoryTest : StringSpec({

    listeners(postgresTestListeners)

    val repository = ExposedUserRepository()
    val defaultSaveEntity = UserSaveEntity(
        name = UserName(NotBlankString.orNull("test")!!),
        registrationTime = Instant.now().toKotlinInstant(),
        permission = PermissionType.SIMPLE
    )

    "entity has been saved" {
        repository.save(defaultSaveEntity)
        transaction { User.count() } shouldBe 1L
    }

    "entity has been updated" {
        val oldSaved = repository.save(defaultSaveEntity)
        val new = UserEditEntity(
            id = oldSaved.id,
            name = UserName(NotBlankString.orNull("new")!!),
            permission = PermissionType.ADMIN
        )

        val result = repository.update(new)

        result shouldBeRight UserEntity(
            id = oldSaved.id,
            name = new.name,
            registrationTime = oldSaved.registrationTime,
            permission = PermissionType.ADMIN
        )
    }

    "error on update if entity doesn't exist" {
        val entity = UserEditEntity(
            id = UserID.orNull(10)!!,
            name = UserName(NotBlankString.orNull("name")!!),
            permission = PermissionType.ADMIN
        )

        val result = repository.update(entity)

        result should beLeft()
    }

    "entity has been deleted" {
        val id = repository.save(defaultSaveEntity).id

        repository.deleteByUserID(id)

        transaction { User.count() } shouldBe 0L
    }

    "safe delete if entity doesn't exist" {
        repository.deleteByUserID(UserID.orNull(10)!!)

        transaction { User.count() } shouldBe 0L
    }

    "entity has been found" {
        val id = repository.save(defaultSaveEntity).id

        val found = repository.findByUserID(id)

        found shouldBe UserEntity(
            id = id,
            name = defaultSaveEntity.name,
            registrationTime = defaultSaveEntity.registrationTime,
            permission = defaultSaveEntity.permission
        )
    }

    "entity has not been found" {
        val found = repository.findByUserID(UserID.orNull(10)!!)

        found shouldBe null
    }

})
