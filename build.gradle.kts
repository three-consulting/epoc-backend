import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
plugins {
    id("org.springframework.boot") version "2.5.6"
    id("org.jlleitschuh.gradle.ktlint") version "10.1.0"
    kotlin("jvm") version "1.5.31"
    kotlin("plugin.allopen") version "1.4.32"
    kotlin("plugin.spring") version "1.5.31"
    kotlin("plugin.jpa") version "1.5.31"
}

val kotlinVersion = "1.5.31"
val jacksonVersion = "2.12.5"
val springBootVersion = "2.5.6"
val junitVersion = "5.8.0"

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
    implementation("org.springframework.boot:spring-boot-starter-validation:$springBootVersion")
    runtimeOnly("org.postgresql:postgresql:42.2.23")
    testImplementation("org.springframework.boot:spring-boot-starter-test:$springBootVersion")
    testImplementation("org.assertj:assertj-core:3.21.0")
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

tasks.getByName<org.springframework.boot.gradle.tasks.bundling.BootBuildImage>("bootBuildImage") {
    environment = mapOf(
        "BPE_DELIM_JAVA_TOOL_OPTIONS" to " ",
        "BPE_APPEND_JAVA_TOOL_OPTIONS" to "-XX:+ExitOnOutOfMemoryError",
        "BPE_APPEND_JAVA_TOOL_OPTIONS" to "-Xms128m",
        "BPE_APPEND_JAVA_TOOL_OPTIONS" to "-Xmx128m",
        "BP_JVM_VERSION" to "11",
    )
    imageName = "ghcr.io/three-consulting/${project.name}"
    docker {
        publishRegistry {
            username = System.getenv("GITHUB_ACTOR")
            password = System.getenv("GITHUB_TOKEN")
            url = "https://ghcr.io/"
        }
    }
}

allOpen {
    annotation("javax.persistence.Entity")
    annotation("javax.persistence.Embeddable")
    annotation("javax.persistence.MappedSuperclass")
}
