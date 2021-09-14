import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
plugins {
    id("org.springframework.boot") version "2.5.4"
    id("org.jlleitschuh.gradle.ktlint") version "10.1.0"
    kotlin("jvm") version "1.5.30"
    kotlin("plugin.spring") version "1.5.30"
    kotlin("plugin.jpa") version "1.5.30"
}

val kotlinVersion = "1.5.30"
val jacksonVersion = "2.12.5"
val springBootVersion = "2.5.4"
val junitVersion = "5.7.2"

group = "three.consulting"
version = "0.0.1"
java.sourceCompatibility = JavaVersion.VERSION_11

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web:$springBootVersion")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa:$springBootVersion")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:$jacksonVersion")
    implementation("org.jetbrains.kotlin:kotlin-reflect:$kotlinVersion")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:$kotlinVersion")
    implementation("javax.validation:validation-api:2.0.1.Final")
    implementation("javax.xml.bind:jaxb-api:2.3.1")
    implementation("org.flywaydb:flyway-core:7.14.1")
    implementation("org.springdoc:springdoc-openapi-ui:1.5.10")
    implementation("io.github.microutils:kotlin-logging:2.0.11")
    runtimeOnly("org.postgresql:postgresql:42.2.23")
    testImplementation("org.springframework.boot:spring-boot-starter-test:$springBootVersion")
    testImplementation("org.assertj:assertj-core:3.20.2")
    testImplementation("org.junit.jupiter:junit-jupiter-engine:$junitVersion")
    testImplementation("org.junit.jupiter:junit-jupiter-api:$junitVersion")
    testImplementation("io.zonky.test:embedded-database-spring-test:2.1.0")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        jvmTarget = "11"
    }
    dependsOn(tasks.ktlintCheck)
}

tasks.withType<Test> {
    useJUnitPlatform()
    dependsOn(tasks.ktlintCheck)
}

ktlint {
    version.set("0.41.0")
    verbose.set(true)
    outputToConsole.set(true)
    coloredOutput.set(true)
    additionalEditorconfigFile.set(file("./editorconfig"))
    filter {
        exclude("**/generated/**")
        include("**/kotlin/**")
    }
}
