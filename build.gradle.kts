import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.8.21"
    kotlin("plugin.spring") version "1.8.21"
    id("org.springframework.boot") version "3.1.0"
    id("io.spring.dependency-management") version "1.1.0"
    id("com.apollographql.apollo3") version "4.0.0-alpha.1"
    application
}

group = "com.fkh.booklibrary"
version = "1.0-SNAPSHOT"

application {
    mainClass.set("com.fkh.booklibrary.BookLibraryKt")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("io.arrow-kt:arrow-core:1.1.2")
    implementation("com.apollographql.apollo3:apollo-runtime:4.0.0-alpha.1")
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-jackson:2.9.0")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.14.2")

    testImplementation("io.mockk:mockk:1.13.3")
    testImplementation("com.ninja-squad:springmockk:3.1.1")
    testImplementation("org.junit.jupiter:junit-jupiter-engine:5.8.2")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.2")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("com.github.tomakehurst:wiremock:3.0.0-beta-5")
    testImplementation("com.apollographql.apollo3:apollo-testing-support:4.0.0-alpha.1")
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "17"
}

apollo {
    service("booklibrary") {
        packageName.set("com.fkh.booklibrary.infrastructure.adapters.output.graphql.model")
    }
    generateKotlinModels.set(true)
}
