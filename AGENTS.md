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

The project follows a standard layered architecture:

1.  **Bot Layer** (`dev.lewischan.weatherbot.bot`): Handles the Telegram bot lifecycle and publishes the command list to Telegram. `TelegramBotConfiguration` injects all `CommandHandler` beans and registers them with the bot dispatcher.
2.  **Handler Layer** (`dev.lewischan.weatherbot.handler`): Processes bot commands. To add a new command:
    - Implement `CommandHandler`.
    - Mark with `@Component`.
    - It will be automatically registered by `TelegramBotConfiguration` and included in the command list published by `TelegramBot`.
3.  **Service Layer** (`dev.lewischan.weatherbot.service`): Contains business logic. Services are typically injected by interface (e.g., `WeatherService`).
4.  **Repository Layer** (`dev.lewischan.weatherbot.repository`): Handles data persistence.
    - **Pattern**: Uses Spring **`JdbcClient`** for lightweight, type-safe SQL execution.
    - **Mapping**: Manual `ResultSet` mapping is preferred over ORM frameworks like JPA.
5.  **Domain and Model Layers**:
    - `dev.lewischan.weatherbot.domain` contains persisted domain entities.
    - `dev.lewischan.weatherbot.model` contains application and external API models.
6.  **Configuration Layer** (`dev.lewischan.weatherbot.configuration`): Defines Spring beans, typed configuration properties, and integration clients.
7.  **Controller Layer** (`dev.lewischan.weatherbot.controller`): Exposes inbound HTTP endpoints, including Telegram webhook handling.
8.  **Infrastructure Layer** (`dev.lewischan.weatherbot.infrastructure`): Contains cross-cutting runtime infrastructure.

## 🔑 Key Conventions

- **Kotlin Extensions**: Use established extensions in `dev.lewischan.weatherbot.extension` for idiomatic code. 
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
