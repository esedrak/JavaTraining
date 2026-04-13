plugins {
    java
    application
    alias(libs.plugins.spotless)
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

application {
    mainClass = "com.javatraining.bank.cli.BankCli"
}

dependencies {
    implementation(libs.picocli)

    // Jackson for JSON serialization/deserialization
    implementation("com.fasterxml.jackson.core:jackson-databind:2.17.2")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.17.2")
}

spotless {
    java {
        googleJavaFormat()
        removeUnusedImports()
        trimTrailingWhitespace()
        endWithNewline()
    }
}
