package dinf.domain

import dinf.types.ArticleID
import dinf.types.UserID

class ArticleNotFoundException(message: String) : RuntimeException(message) {

    constructor(authorID: UserID , articleID: ArticleID) : this(
        "Found no article with id=$articleID for user with id=$authorID"
    )

    constructor(articleID: ArticleID) : this(
        "Found no article with id=$articleID"
    )

}
