# Weather Bot for Telegram

## Dependency Management

Dependencies are locked using gradle. To update a dependency, modify the version in `build.gradle`,
and update the lock files using the following command.

```shell
./gradlew dependencies --write-locks
```

## Gradle

To update Gradle, use the command. You may optionally replace `latest` with a version like `8.9`.

```shell
./gradlew wrapper --gradle-version latest
```
