import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
plugins {
    id("org.springframework.boot") version "2.7.2"
    id("org.jlleitschuh.gradle.ktlint") version "10.2.1"
    kotlin("jvm") version "1.7.10"
    kotlin("plugin.allopen") version "1.7.10"
    kotlin("plugin.spring") version "1.7.10"
    kotlin("plugin.jpa") version "1.7.10"
}

val kotlinVersion = "1.7.10"
val jacksonVersion = "2.13.3"
val springBootVersion = "2.7.2"
val junitVersion = "5.9.0"

group = "three.consulting"
java.sourceCompatibility = JavaVersion.VERSION_17

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web:$springBootVersion")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa:$springBootVersion")
    implementation("org.springframework.boot:spring-boot-starter-validation:$springBootVersion")
    implementation("org.springframework.boot:spring-boot-starter-oauth2-resource-server:$springBootVersion")
    implementation("org.springframework.boot:spring-boot-starter-security:$springBootVersion")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:$jacksonVersion")
    implementation("org.jetbrains.kotlin:kotlin-reflect:$kotlinVersion")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:$kotlinVersion")
    implementation("javax.validation:validation-api:2.0.1.Final")
    implementation("javax.xml.bind:jaxb-api:2.3.1")
    implementation("org.flywaydb:flyway-core:8.5.13")
    implementation("org.springdoc:springdoc-openapi-ui:1.6.9")
    implementation("io.github.microutils:kotlin-logging-jvm:2.1.23")
    runtimeOnly("org.postgresql:postgresql:42.3.6")
    testImplementation("org.springframework.boot:spring-boot-starter-test:$springBootVersion")
    testImplementation("org.assertj:assertj-core:3.23.1")
    testImplementation("org.junit.jupiter:junit-jupiter-engine:$junitVersion")
    testImplementation("org.junit.jupiter:junit-jupiter-api:$junitVersion")
    testImplementation("io.zonky.test:embedded-database-spring-test:2.1.1")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        jvmTarget = "17"
    }
    dependsOn(tasks.ktlintCheck)
}

tasks.withType<Test> {
    useJUnitPlatform()
    dependsOn(tasks.ktlintCheck)
}

ktlint {
    version.set("0.43.2")
    verbose.set(true)
    outputToConsole.set(true)
    coloredOutput.set(true)
    additionalEditorconfigFile.set(file("./editorconfig"))
    filter {
        exclude("**/generated/**")
        include("**/kotlin/**")
    }
}

allOpen {
    annotation("javax.persistence.Entity")
    annotation("javax.persistence.Embeddable")
    annotation("javax.persistence.MappedSuperclass")
}
