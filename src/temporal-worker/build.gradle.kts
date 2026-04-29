plugins {
    java
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.spring.dependency.management)
    alias(libs.plugins.spotless)
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

dependencies {
    // Workflow interfaces and DTOs
    implementation(project(":src:temporal-workflow"))

    // Bank domain — activities use BankService / repositories
    implementation(project(":src:bank-api"))

    // Temporal SDK
    implementation(libs.temporal.sdk)

    // Spring Boot
    implementation("org.springframework.boot:spring-boot-starter")
    implementation(libs.spring.boot.starter.data.jpa)
    implementation(libs.postgresql)

    // Testing
    testImplementation(libs.spring.boot.starter.test)
    testImplementation(libs.assertj)
    testImplementation(libs.temporal.testing)
    testImplementation(libs.mockito.core)
    testImplementation(libs.mockito.junit.jupiter)
}

tasks.test {
    useJUnitPlatform()
}

spotless {
    java {
        googleJavaFormat()
        removeUnusedImports()
        trimTrailingWhitespace()
        endWithNewline()
    }
}
