dependencyResolutionManagement {
    @Suppress("UnstableApiUsage")
    repositories {
        mavenCentral()
        maven {
            url = uri("https://jitpack.io")
            content {
                includeModule("io.github.kotlin-telegram-bot.kotlin-telegram-bot", "telegram")
            }
        }
        repositoriesMode = RepositoriesMode.FAIL_ON_PROJECT_REPOS
    }
}

rootProject.name = "weather-bot"
