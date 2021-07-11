package dinf.types

sealed class ArticleError

object ArticleNoPermissionError : ArticleError()

object ArticleNotFoundError : ArticleError()
