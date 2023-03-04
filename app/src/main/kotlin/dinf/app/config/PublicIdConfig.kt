package dinf.app.config

data class PublicIdConfig(
    val dice: HashConfig = HashConfig("salt", 6),
    val edge: HashConfig = HashConfig("salt", 6),
    val user: HashConfig = HashConfig("salt", 6),
)
