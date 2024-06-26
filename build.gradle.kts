plugins {
    id("org.springframework.boot") version "3.3.0"
    id("com.github.ben-manes.versions") version "0.51.0"
    kotlin("jvm") version "1.9.24"
    kotlin("plugin.allopen") version "1.9.24"
    kotlin("plugin.spring") version "1.9.24"
    kotlin("plugin.jpa") version "1.9.24"
}

val kotlinVersion = "1.9.24"
val jacksonVersion = "2.17.1"
val springBootVersion = "3.3.0"
val junitVersion = "5.10.2"

val ktlint: Configuration by configurations.creating

group = "three.consulting"
java.sourceCompatibility = JavaVersion.VERSION_21

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web:$springBootVersion")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa:$springBootVersion")
    implementation("org.springframework.boot:spring-boot-starter-validation:$springBootVersion")
    implementation("org.springframework.boot:spring-boot-starter-oauth2-resource-server:$springBootVersion")
    implementation("org.springframework.boot:spring-boot-starter-security:$springBootVersion")
    implementation("org.springframework.boot:spring-boot-starter-validation:$springBootVersion")
    implementation("org.springframework.boot:spring-boot-starter-actuator:$springBootVersion")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:$jacksonVersion")
    implementation("org.jetbrains.kotlin:kotlin-reflect:$kotlinVersion")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:$kotlinVersion")
    implementation("jakarta.persistence:jakarta.persistence-api:3.1.0")
    implementation("org.flywaydb:flyway-core:9.22.3")
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.5.0")
    implementation("io.github.microutils:kotlin-logging-jvm:3.0.5")
    implementation("com.google.firebase:firebase-admin:9.3.0")

    runtimeOnly("org.postgresql:postgresql:42.7.3")

    testImplementation("org.springframework.boot:spring-boot-starter-test:$springBootVersion")
    testImplementation("org.assertj:assertj-core:3.26.0")
    testImplementation("org.junit.jupiter:junit-jupiter-engine:$junitVersion")
    testImplementation("org.junit.jupiter:junit-jupiter-api:$junitVersion")
    testImplementation("io.zonky.test:embedded-database-spring-test:2.5.1")

    ktlint("com.pinterest:ktlint:0.51.0-FINAL") {
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
            jvmTarget = JavaVersion.VERSION_21.toString()
        }
        dependsOn(ktlintCheck)
    }

    compileTestKotlin {
        kotlinOptions {
            jvmTarget = JavaVersion.VERSION_21.toString()
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
