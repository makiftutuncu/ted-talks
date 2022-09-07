plugins {
    id("application")
    id("java")
    id("org.springframework.boot") version "2.7.3"
    id("io.freefair.lombok") version "6.5.0.3"
}

group = "dev.akif"
version = "1.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.flywaydb:flyway-core:9.2.0")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa:2.7.3")
    implementation("org.springframework.boot:spring-boot-starter-web:2.7.3")
    implementation("org.springdoc:springdoc-openapi-ui:1.6.11")
    runtimeOnly("javax.xml.bind:jaxb-api:2.3.1")
    runtimeOnly("org.postgresql:postgresql:42.5.0")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.9.0")
    testImplementation("org.springframework.boot:spring-boot-starter-test:2.7.3")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.9.0")
}

application {
    mainClass.set("dev.akif.tedtalks.Main")
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(18))
    }
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
    testLogging {
        events          = setOf(
                org.gradle.api.tasks.testing.logging.TestLogEvent.PASSED,
                org.gradle.api.tasks.testing.logging.TestLogEvent.SKIPPED,
                org.gradle.api.tasks.testing.logging.TestLogEvent.FAILED
        )
        showCauses      = true
        showExceptions  = true
        showStackTraces = true
    }
}
