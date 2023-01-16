plugins {
    id("org.springframework.boot") version "3.0.1"
    id("com.github.ben-manes.versions") version "0.44.0"
    kotlin("jvm") version "1.8.0"
    kotlin("plugin.allopen") version "1.8.0"
    kotlin("plugin.spring") version "1.8.0"
    kotlin("plugin.jpa") version "1.8.0"
}

val kotlinVersion = "1.8.0"
val jacksonVersion = "2.14.1"
val springBootVersion = "3.0.1"
val junitVersion = "5.9.2"

val ktlint: Configuration by configurations.creating

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
    implementation("jakarta.persistence:jakarta.persistence-api:3.1.0")
    implementation("jakarta.validation:jakarta.validation-api:3.0.2")
    implementation("org.flywaydb:flyway-core:9.11.0")
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.0.2")
    implementation("io.github.microutils:kotlin-logging-jvm:3.0.4")
    implementation("com.google.firebase:firebase-admin:9.1.1")

    runtimeOnly("org.postgresql:postgresql:42.5.1")

    testImplementation("org.springframework.boot:spring-boot-starter-test:$springBootVersion")
    testImplementation("org.assertj:assertj-core:3.24.1")
    testImplementation("org.junit.jupiter:junit-jupiter-engine:$junitVersion")
    testImplementation("org.junit.jupiter:junit-jupiter-api:$junitVersion")
    testImplementation("io.zonky.test:embedded-database-spring-test:2.2.0")

    ktlint("com.pinterest:ktlint:0.48.1") {
        attributes {
            attribute(Bundling.BUNDLING_ATTRIBUTE, objects.named(Bundling.EXTERNAL))
        }
    }
}

allOpen {
    annotation("jakarta.persistence.Entity")
    annotation("jakarta.persistence.Embeddable")
    annotation("jakarta.persistence.MappedSuperclass")
}

tasks {
    val ktlintFormat by creating(JavaExec::class) {
        inputs.files(project.sourceSets.main.get().allSource)

        classpath = ktlint
        mainClass.set("com.pinterest.ktlint.Main")
        args = listOf("-F", "src/**/*.kt", "!src/**/generated/**")
        jvmArgs("--add-opens", "java.base/java.lang=ALL-UNNAMED")
    }

    val ktlintCheck by creating(JavaExec::class) {
        inputs.files(project.sourceSets.main.get().allSource)

        classpath = ktlint
        mainClass.set("com.pinterest.ktlint.Main")
        args = listOf("src/**/*.kt", "!src/**/generated/**", "--color", "--color-name=LIGHT_RED")
    }

    compileKotlin {
        kotlinOptions {
            jvmTarget = JavaVersion.VERSION_17.toString()
        }
        dependsOn(ktlintCheck)
    }

    compileTestKotlin {
        kotlinOptions {
            jvmTarget = JavaVersion.VERSION_17.toString()
        }
    }

    test {
        useJUnitPlatform()
        dependsOn(ktlintCheck)
    }

    dependencyUpdates {
        rejectVersionIf {
            val stableKeyword = listOf("RELEASE", "FINAL", "GA").any { candidate.version.contains(it, ignoreCase = true) }
            val regex = "^[0-9,.v-]+(-r)?$".toRegex()
            val isStable = stableKeyword || regex.matches(candidate.version)
            isStable.not()
        }
    }
}
