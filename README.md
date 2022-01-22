![dInf](backend/src/main/resources/img/dinf.png)

**dInf** (infinity dice) - project for creating random generators and sharing them with others.

Sample website: [dinf.ustits.dev](https://dinf.ustits.dev)

## Configuration

To overload [log configuration](backend/src/main/resources/logback.xml) pass `logback.configurationFile` 
system property. For example via `JAVA_OPTS` environment variable:

```shell
export JAVA_OPTS='-Dlogback.configurationFile=/path/to/config.xml'
```

To enable call logging:

```xml
<logger name="ktor.application" level="DEBUG"/>
```
