# Weather Bot for Telegram - Agent Instructions

This guide is for AI agents and developers who are interacting with this repository. It provides context on the architecture, technical stack, and contribution guidelines to ensure consistent and high-quality changes.

## 🛠️ Technical Stack

- **Language**: [Kotlin](https://kotlinlang.org/) (JVM)
- **Java**: 25
- **Framework**: [Spring Boot](https://spring.io/projects/spring-boot)
- **Build System**: [Gradle](https://gradle.org/) (with Kotlin DSL and Dependency Locking)
- **Database**: [PostgreSQL](https://www.postgresql.org/)
- **Migrations**: [Flyway](https://flywaydb.org/)
- **Testing**: [Kotest](https://kotest.io/) with [MockK](https://mockk.io/) and [WireMock](https://wiremock.org/)
- **Integrations**:
  - **Telegram**: [`kotlin-telegram-bot`](https://github.com/kotlin-telegram-bot/kotlin-telegram-bot)
  - **Location**: [Google Maps Services SDK](https://github.com/googlemaps/google-maps-services-java)
  - **Weather**: [OpenMeteo API](https://open-meteo.com/) (Direct REST)

## 🏗️ Architecture

The project is organized by application core, location, external platform, and provider ownership. Each area retains layered subpackages where appropriate:

1.  **Core** (`dev.lewischan.weatherbot.core`): Contains the application entry point and platform-independent configuration, domain entities, models, repositories, services, helpers, extensions, and infrastructure.
    - Repository implementations use Spring **`JdbcClient`** with manual `ResultSet` mapping rather than ORM frameworks.
    - `WeatherBotApplication` scans `dev.lewischan.weatherbot` so core, platform, and provider beans are discovered.
2.  **Core Location and Weather**: Shared location models and services are under `dev.lewischan.weatherbot.core.location`. The weather service contract is under `dev.lewischan.weatherbot.core.weather`, with shared weather models under `dev.lewischan.weatherbot.core.weather.model`.
3.  **Telegram Platform** (`dev.lewischan.weatherbot.platforms.telegram`): Contains Telegram-specific bot, configuration, controller, error, extension, handler, and service packages. `TelegramBotConfiguration` injects all `CommandHandler` beans and registers them with the bot dispatcher. To add a new command:
    - Implement `CommandHandler`.
    - Mark with `@Component`.
    - It will be automatically registered by `TelegramBotConfiguration` and included in the command list published by `TelegramBot`.
4.  **Location Providers** (`dev.lewischan.weatherbot.providers.location`): Google Maps configuration and integration are under `dev.lewischan.weatherbot.providers.location.googlemaps`.
5.  **Weather Providers** (`dev.lewischan.weatherbot.providers.weather`): OpenMeteo and PirateWeather configuration, API models, and service integrations are under `dev.lewischan.weatherbot.providers.weather.openmeteo` and `dev.lewischan.weatherbot.providers.weather.pirateweather`. Telegram handlers select the concrete provider services directly.
6.  **Tests**: Mirror production ownership, including core weather model tests and provider integration tests under their corresponding packages. Shared integration-test infrastructure is under `dev.lewischan.weatherbot.core.test`.

## 🔑 Key Conventions

- **Kotlin Extensions**: Use established extensions in `dev.lewischan.weatherbot.core.extension` and platform-specific extensions such as `dev.lewischan.weatherbot.platforms.telegram.extension` for idiomatic code.
  - e.g., Use `Bot.replyMessage` instead of the raw Telegram SDK call for consistent error handling.
- **Dependency Updates**: Use `make update-dependencies` after modifying `gradle/libs.versions.toml` or dependency declarations.
  - **Note**: This project uses **STRICT dependency locking**. Keep `gradle.lockfile`, `buildscript-gradle.lockfile`, and `settings-gradle.lockfile` in sync with dependency changes.
- **Database Migrations**: Add new SQL scripts to `src/main/resources/db/migration/` using the `V<N>__<description>.sql` format.
- **Testing**:
  - Unit tests use Kotest and MockK.
  - Integration tests extend `BaseIntTest` for pre-configured Spring context.
  - **WireMock Assets**: External API mocks (JSON) are located in `src/test/resources/`.
- **External APIs**:
  - Use the established `RestClient` pattern or official SDKs.
  - **Google Maps Platform (GMP)**:
    - **Best Practices**: Always prioritize the official Google Maps Services Java SDK.
    - **Documentation**: Agents should use available documentation retrieval tools to verify current API capabilities before implementation.
    - **Security**: Never hardcode API keys. Use the established `GoogleMapsServicesProperties` configuration.

## 🚀 Development Workflows

- **CI/CD**: GitHub Actions runs on every PR. **SonarCloud** provides quality gate analysis.
- **Environment**: Configuration is managed via `application.yaml` and profile-specific overrides (e.g., `application-development.yaml`).
- **Local Run**: `make dev-run` (uses `development` profile).
- **Build**: `make build` or `./gradlew build`.
- **Docker**: `make build-dev-image` to build a local image for testing.

## ⚠️ Git & Source Control Rules

- **No Automatic Commits/Pushes**: **NEVER** commit or push changes automatically unless explicitly directed by the user.
- **Branching Strategy**: **NEVER** make changes directly on `main`. If currently on `main`, create a focused branch before editing. Continue on an existing user-created feature branch unless directed otherwise.
- **Working Tree Safety**: Preserve unrelated user changes. Do not overwrite or revert files outside the requested scope.
- **Destructive Commands**: Do not use destructive Git commands such as `git reset --hard` or `git checkout --` unless the user explicitly requests them.
- **Review**: Always propose a plan and show the diff before asking to commit.

## 🤖 Agent Workflow

When making changes:
1.  **Research**: Map the relevant services and handlers.
2.  **Strategy**: Plan implementation, including necessary model changes and service updates.
3.  **Tests**: Add or update Kotest specs for behavioral changes. Documentation-only and other non-behavioral changes do not require new tests.
4.  **Verification**: Run the narrowest relevant checks while iterating, then run `./gradlew build` for code changes. `./gradlew test` runs both unit tests and `*IntTest` integration tests but does not replace the complete build verification.
