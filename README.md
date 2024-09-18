# Weather Bot for Telegram

## Dependency Management

Dependencies are locked using gradle. To update a dependency, modify the version in 
[/gradle/libs.versions.toml](/gradle/libs.versions.toml), and update the lock files using the following command.

```shell
./gradlew dependencies --write-locks
```

## Gradle

To update Gradle, replace `$(NEW-VERSION)` with the version to update to.

```shell
make update-gradle new-version=$(NEW-VERSION)
```

## Environment Variables

These variables must be set for the bot to function normally.

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

## Development Test Image

### Build Docker Image

```shell
make build-dev-image
```

### Run Container

Duplicate the [.env.example](/.env.example) file with the name `.env.development`, use the following command.

```shell
make run-dev-container
```

### Stop Container

```shell
make stop-dev-container
```

### Delete Container

```shell
make rm-dev-container
```

### Stop and Delete Container

```shell
make stop-rm-dev-container
```
