# Makefile for Weather Bot for Telegram

# Variables
GRADLEW = ./gradlew

# Default target
.PHONY: all
all: build

# Build the project
.PHONY: build
build:
	$(GRADLEW) build

# Build development test container
.PHONY: build-dev-container
build-dev-container: build
	docker build -t weather-bot:dev .

# Update dependencies
.PHONY: update-dependencies
update-dependencies:
	$(GRADLEW) dependencies --write-locks

# Update Gradle wrapper
.PHONY: update-gradle
update-gradle:
	$(GRADLEW) wrapper --gradle-version latest

# Help target to list all available commands
.PHONY: help
help:
	@echo "Makefile commands:"
	@echo "  make build                - Build the project"
	@echo "  make build-dev-container  - Build the development test container"
	@echo "  make update-dependencies  - Update dependencies and write lock files"
	@echo "  make update-gradle        - Update Gradle to the latest version"
	@echo "  make help                 - Display this help message"