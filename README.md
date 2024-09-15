# Weather Bot for Telegram

## Environment Variables

```dotenv
# Google Maps API Key
ENV_GOOGLE_MAPS_PLATFORM_API_KEY=google-maps-geocoding-api-key

# Database Configuration
ENV_DB_HOST=postgres-database-host
ENV_DB_PORT=postgres-database-port
ENV_DB_NAME=postgres-database-name
ENV_DB_USERNAME=postgres-database-username
ENV_DB_PASSWORD=postgres-database-password

# Telegram Bot Configuration
ENV_TELEGRAM_BOT_API_TOKEN=telegram-bot-api-token
ENV_TELEGRAM_BOT_SERVER_HOSTNAME=telegram-bot-server-hostname
ENV_TELEGRAM_BOT_USE_WEBHOOK=true-or-false
```

## Dependency Management

Dependencies are locked using gradle. To update a dependency, modify the version in 
[/gradle/libs.versions.toml](/gradle/libs.versions.toml), and update the lock files using the following command.

```shell
./gradlew dependencies --write-locks
```

## Gradle

To update Gradle, use the command. You may optionally replace `latest` with a version like `8.9`.

```shell
./gradlew wrapper --gradle-version latest
```

## Build Development Test Container

```shell
./gradlew build && docker build -t weather-bot:dev .
```
