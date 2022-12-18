plugins {
	kotlin("jvm") version "1.7.20"
}

group = "org.dzhyrma"
version = "1.0-SNAPSHOT"

java.sourceCompatibility = JavaVersion.VERSION_11

repositories {
	mavenCentral()
}

dependencies {
	implementation("com.github.gantsign.maven:ktlint-maven-plugin:1.15.2")

	testImplementation("org.junit.jupiter:junit-jupiter-engine:5.9.0")
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs = listOf("-Xjsr305=strict")
		jvmTarget = "11"
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}
