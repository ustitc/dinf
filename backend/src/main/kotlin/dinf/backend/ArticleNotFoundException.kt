package dinf.backend

import dinf.data.exposed.UserEntity
import dinf.types.ArticleID

class ArticleNotFoundException(message: String) : RuntimeException(message) {

    constructor(userEntity: UserEntity, articleID: ArticleID) : this(
        authorID = userEntity.id.value, articleID = articleID.toInt()
    )

    constructor(authorID: Int , articleID: Int) : this(
        "Found no article with id=$articleID for user with id=$authorID"
    )

    constructor(articleID: ArticleID) : this(
        "Found no article with id=$articleID"
    )

}
