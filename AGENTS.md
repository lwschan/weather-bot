# Weather Bot for Telegram - Agent Instructions

This guide is for AI agents and developers who are interacting with this repository. It provides context on the architecture, technical stack, and contribution guidelines to ensure consistent and high-quality changes.

## 🛠️ Technical Stack

- **Language**: [Kotlin](https://kotlinlang.org/) (JVM)
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

1.  **Bot Layer** (`dev.lewischan.weatherbot.bot`): Handles Telegram bot lifecycle. `TelegramBot` automatically registers all `CommandHandler` implementations via list injection.
2.  **Handler Layer** (`dev.lewischan.weatherbot.handler`): Processes bot commands. To add a new command:
    - Implement `CommandHandler`.
    - Mark with `@Component`.
    - It will be automatically registered by `TelegramBot`.
3.  **Service Layer** (`dev.lewischan.weatherbot.service`): Contains business logic. Services are typically injected by interface (e.g., `WeatherService`).
4.  **Repository Layer** (`dev.lewischan.weatherbot.repository`): Handles data persistence.
    - **Pattern**: Uses Spring **`JdbcClient`** for lightweight, type-safe SQL execution.
    - **Mapping**: Manual `ResultSet` mapping is preferred over ORM frameworks like JPA.
5.  **Model Layer** (`dev.lewischan.weatherbot.model`): Defines domain and API models.

## 🔑 Key Conventions

- **Kotlin Extensions**: Use established extensions in `dev.lewischan.weatherbot.extension` for idiomatic code. 
  - e.g., Use `Bot.sendMessage` instead of the raw Telegram SDK call for consistent error handling.
- **Dependency Updates**: Use `make update-dependencies` after modifying `libs.versions.toml`. 
  - **Note**: This project uses **STRICT dependency locking**. The build will fail if `gradle.lockfile` or `buildscript-gradle.lockfile` are out of sync with the dependencies.
- **Database Migrations**: Add new SQL scripts to `src/main/resources/db/migration/` using the `V<N>__<description>.sql` format.
- **Testing**:
  - Unit tests use Kotest and MockK.
  - Integration tests extend `BaseIntTest` for pre-configured Spring context.
  - **WireMock Assets**: External API mocks (JSON) are located in `src/test/resources/`.
- **External APIs**:
  - Use the established `RestClient` pattern or official SDKs (like Google Maps).
  - **Google Maps**: Specialized `GEMINI.md` extension is available for advanced location service tools.

## 🚀 Development Workflows

- **CI/CD**: GitHub Actions runs on every PR. **SonarCloud** provides quality gate analysis.
- **Environment**: Configuration is managed via `application.yaml` and profile-specific overrides (e.g., `application-development.yaml`).
- **Local Run**: `make dev-run` (uses `development` profile).
- **Build**: `make build` or `./gradlew build`.
- **Docker**: `make build-dev-image` to build a local image for testing.

## 🤖 Agent Workflow

When making changes:
1.  **Research**: Map the relevant services and handlers.
2.  **Strategy**: Plan implementation, including necessary model changes and service updates.
3.  **Tests**: Always add or update Kotest specs for any behavioral change.
4.  **Verification**: Run `./gradlew test` to ensure all tests pass.
