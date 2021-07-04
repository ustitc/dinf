package dinf.data.exposed

import dinf.data.UserEditEntity
import dinf.data.UserEntity
import dinf.data.UserSaveEntity
import dinf.types.NotBlankString
import dinf.types.UserName
import io.kotest.assertions.arrow.either.shouldBeRight
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.Instant

class ExposedUserRepositoryTest : StringSpec({

    listeners(postgresTestListeners)

    val repository = ExposedUserRepository()

    "entity has been saved" {
        repository.save(
            UserSaveEntity(
                name = UserName(NotBlankString.orNull("test")!!),
                registrationTime = Instant.now()
            )
        )
        transaction { User.count() } shouldBe 1L
    }

    "entity has been updated" {
        val oldSaved = repository.save(
            UserSaveEntity(
                name = UserName(NotBlankString.orNull("test")!!),
                registrationTime = Instant.now()
            )
        )
        val new = UserEditEntity(
            id = oldSaved.id,
            name = UserName(NotBlankString.orNull("new")!!)
        )

        val result = repository.update(new)

        result shouldBeRight UserEntity(
            id = oldSaved.id,
            name = new.name,
            registrationTime = oldSaved.registrationTime
        )
    }

    "entity has been deleted" {
        val id = repository.save(
            UserSaveEntity(
                name = UserName(NotBlankString.orNull("test")!!),
                registrationTime = Instant.now()
            )
        ).id

        repository.deleteByUserID(id)

        transaction { User.count() } shouldBe 0L
    }

})
