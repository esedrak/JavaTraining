plugins {
    java
    alias(libs.plugins.spring.boot) apply false
    alias(libs.plugins.spring.dependency.management)
    alias(libs.plugins.spotless)
}

dependencyManagement {
    imports {
        mavenBom("org.springframework.boot:spring-boot-dependencies:${libs.versions.spring.boot.get()}")
    }
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

dependencies {
    // Spring context (for DI/Configuration/Scheduled topics)
    implementation("org.springframework:spring-context")
    implementation("org.springframework.boot:spring-boot-starter")

    // Spring Web (for HTTP/REST topics)
    implementation("org.springframework.boot:spring-boot-starter-web")

    // Reactor Core (for ReactiveStreams topic)
    implementation(libs.reactor.core)

    // Testing
    testImplementation(libs.spring.boot.starter.test)
    testImplementation(libs.assertj)
    testImplementation(libs.mockito.core)
    testImplementation(libs.mockito.junit.jupiter)
    testImplementation(libs.reactor.test)
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
