# Makefile for Weather Bot for Telegram

# Variables
GRADLEW_UNIX = ./gradlew
GRADLEW_WIN = gradlew.bat
GRADLEW = $(GRADLEW_UNIX)
ifeq ($(OS),Windows_NT)
    GRADLEW = $(GRADLEW_WIN)
endif

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

# Help command to list available targets
.PHONY: help
help:
	@echo "Usage: make [target]"
	@echo
	@echo "Targets:"
	@echo "  all                      Build the project (default)"
	@echo "  build                    Build the project using Gradle"
	@echo "  build-dev-image          Build the development Docker image"
	@echo "  run-dev-container        Run the development Docker container"
	@echo "  stop-dev-container       Stop the development Docker container"
	@echo "  rm-dev-container         Remove the development Docker container"
	@echo "  stop-rm-dev-container    Stop and remove the development Docker container"
	@echo "  update-dependencies      Update the project's dependencies"
	@echo "  update-gradle            Update the Gradle wrapper version"