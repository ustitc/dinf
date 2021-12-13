# Service

Configurations are stored in toml format. To overload [defaults](./src/main/resources/application.toml)
specify `CONFIG_PATH` environment variable with your configuration. 

## Samples

SQLite

```toml
[database]
jdbcURL = "jdbc:sqlite:dinf.db"
```

Postgres

```toml
[database]
jdbcURL = "jdbc:postgresql://localhost:5432/dinf?user=dinf_admin&password=dinf_password"
```
