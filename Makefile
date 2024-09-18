# Makefile for Weather Bot for Telegram

# Variables
GRADLEW = ./gradlew
DEV-IMAGE-NAME = weather-bot-dev

# Default target
.PHONY: all
all: build

# Build the project
.PHONY: build
build:
	$(GRADLEW) build

# Build development test image
.PHONY: build-dev-image
build-dev-image: build
	docker build -t weather-bot:dev .

# Run development test container
.PHONY: run-dev-container
run-dev-container:
	docker run -it --env-file .env.development --name $(DEV-IMAGE-NAME) --detach weather-bot:dev

# Stop development test container
.PHONY: stop-dev-container
stop-dev-container:
	docker stop $(DEV-IMAGE-NAME)

# Remove development test container
.PHONY: rm-dev-container
rm-dev-container:
	docker rm $(DEV-IMAGE-NAME)

# Stop and remove development test container
.PHONY: stop-rm-dev-container
stop-rm-dev-container:
	docker stop $(DEV-IMAGE-NAME) && docker rm $(DEV-IMAGE-NAME)

# Update dependencies
.PHONY: update-dependencies
update-dependencies:
	$(GRADLEW) dependencies --write-locks

# Update Gradle wrapper
.PHONY: update-gradle
update-gradle:
	$(GRADLEW) wrapper --gradle-version $(new-version)

# Help target to list all available commands
.PHONY: help
help:
	@echo "Makefile commands:"
	@echo "  make build                - Build the project"
	@echo "  make build-dev-container  - Build the development test container"
	@echo "  make update-dependencies  - Update dependencies and write lock files"
	@echo "  make update-gradle        - Update Gradle to the latest version"
	@echo "  make help                 - Display this help message"