import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
plugins {
    id("org.springframework.boot") version "2.5.6"
    id("org.jlleitschuh.gradle.ktlint") version "10.1.0"
    kotlin("jvm") version "1.5.31"
    kotlin("plugin.allopen") version "1.5.31"
    kotlin("plugin.spring") version "1.5.31"
    kotlin("plugin.jpa") version "1.5.31"
}

val kotlinVersion = "1.5.31"
val jacksonVersion = "2.13.0"
val springBootVersion = "2.5.6"
val junitVersion = "5.8.1"

group = "three.consulting"
java.sourceCompatibility = JavaVersion.VERSION_17

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
    implementation("org.flywaydb:flyway-core:8.0.2")
    implementation("org.springdoc:springdoc-openapi-ui:1.5.12")
    implementation("io.github.microutils:kotlin-logging:2.0.11")
    implementation("org.springframework.boot:spring-boot-starter-validation:$springBootVersion")
    runtimeOnly("org.postgresql:postgresql:42.3.1")
    testImplementation("org.springframework.boot:spring-boot-starter-test:$springBootVersion")
    testImplementation("org.assertj:assertj-core:3.21.0")
    testImplementation("org.junit.jupiter:junit-jupiter-engine:$junitVersion")
    testImplementation("org.junit.jupiter:junit-jupiter-api:$junitVersion")
    testImplementation("io.zonky.test:embedded-database-spring-test:2.1.1")
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
    version.set("0.42.1")
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
