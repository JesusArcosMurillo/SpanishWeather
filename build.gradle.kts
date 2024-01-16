// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "8.1.2" apply false
    id("org.jetbrains.kotlin.android") version "1.9.0" apply false
    id("com.google.devtools.ksp") version "1.9.10-1.0.13" apply false
    id("androidx.navigation.safeargs.kotlin") version "2.5.3" apply false
    id ("org.sonarqube") version "4.4.1.3373"
}

sonar {
    properties {
        property ("sonar.projectKey", "JesusArcosMurillo_SpanishWeather")
        property ("sonar.organization", "jesusarcosmurillo")
        property ("sonar.host.url", "https://sonarcloud.io")
    }
}