# App

Configurations are stored in toml format. To overload [defaults](./src/main/resources/application.toml)
specify `CONFIG_PATH` environment variable with your configuration. 

## Samples

```toml
[database]
jdbcURL = "jdbc:sqlite:dinf.db"
```

