plugins {
    alias(libs.plugins.spotless) apply false
}

allprojects {
    group = "com.javatraining"
    version = "0.1.0"

    repositories {
        mavenCentral()
    }
}
