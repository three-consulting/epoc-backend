import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("org.springframework.boot") version "2.5.4"
	id("io.spring.dependency-management") version "1.0.11.RELEASE"
	kotlin("jvm") version "1.5.30"
	kotlin("plugin.spring") version "1.5.30"
	kotlin("plugin.jpa") version "1.5.30"
}

val kotlinVersion = project.properties["kotlinVersion"]
val jacksonVersion = project.properties["jacksonVersion"]
val springBootVersion = project.properties["springBootVersion"]

group = "three.consulting"
version = "0.0.1"
java.sourceCompatibility = JavaVersion.VERSION_11

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-web:${springBootVersion}")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa:${springBootVersion}")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin:${jacksonVersion}")
	implementation("org.jetbrains.kotlin:kotlin-reflect:${kotlinVersion}")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:${kotlinVersion}")
	testImplementation("org.springframework.boot:spring-boot-starter-test:${springBootVersion}")
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		jvmTarget = "11"
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}
