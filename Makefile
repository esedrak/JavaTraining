.PHONY: build test run-bank-api run-hello run-bank-cli run-worker run-client \
        infra-up infra-down db-migrate fmt lint clean help

build:
	./gradlew build

test:
	./gradlew test

run-bank-api:
	./gradlew :src:bank-api:bootRun

run-hello:
	./gradlew :src:hello:bootRun

run-bank-cli:
	./gradlew :src:bank-cli:run --args="$(ARGS)"

run-worker:
	./gradlew :src:temporal-worker:bootRun

run-client:
	./gradlew :src:temporal-client:run

infra-up:
	docker compose up -d

infra-down:
	docker compose down

db-migrate:
	./gradlew :src:bank-repository:flywayMigrate

fmt:
	./gradlew spotlessApply

lint:
	./gradlew spotlessCheck

clean:
	./gradlew clean

help:
	@grep -E '^[a-zA-Z_-]+:' Makefile | sort
