// Pure-Java module: workflow interfaces and shared DTOs.
// No Spring Boot — this jar is depended on by both temporal-worker and bank-api.
plugins {
    java
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

dependencies {
    implementation(libs.temporal.sdk)
}
