plugins {
	kotlin("jvm") version "2.2.20"
	kotlin("plugin.allopen") version "2.2.20"
	id("io.quarkus") version "3.29.2"
}

group = "com.damian.marketgrid"
version = "0.0.1-SNAPSHOT"
description = "Ingest Service"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(23)
	}
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation(enforcedPlatform("io.quarkus:quarkus-bom:3.29.2"))
	implementation("io.quarkus:quarkus-kotlin")
	implementation("io.quarkus:quarkus-arc")
	implementation("io.quarkus:quarkus-config-yaml")

    implementation("io.quarkus:quarkus-rest-jackson")
    implementation("io.quarkus:quarkus-rest-client")
    implementation("io.quarkus:quarkus-rest-client-jackson")

    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.20.1")
    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-yaml:2.20.0")
    implementation("org.jsoup:jsoup:1.21.2")

    implementation("io.quarkus:quarkus-jdbc-mysql")
    implementation("io.quarkus:quarkus-flyway")
    implementation("io.quarkiverse.jooq:quarkus-jooq:2.1.0")

    implementation("io.quarkus:quarkus-scheduler")

    implementation("org.apache.kafka:kafka-clients:4.1.0")
    implementation("io.quarkus:quarkus-smallrye-health")

    testImplementation("io.rest-assured:rest-assured:5.5.6")
    testImplementation("io.quarkus:quarkus-junit5")
    testImplementation("io.quarkus:quarkus-jdbc-h2")

    testImplementation("io.cucumber:cucumber-java:7.31.0")
    testImplementation("io.cucumber:cucumber-junit-platform-engine:7.31.0")
	testImplementation("io.quarkiverse.cucumber:quarkus-cucumber:1.3.0")
    testImplementation("org.junit.platform:junit-platform-suite:6.0.1")
}

kotlin {
	compilerOptions {
		freeCompilerArgs.addAll("-Xjsr305=strict", "-Xjvm-default=all")
	}
}

allOpen {
	annotation("jakarta.enterprise.context.ApplicationScoped")
	annotation("jakarta.persistence.Entity")
}

quarkus {
	finalName = "ingest-service"
}

tasks.withType<JavaExec> {
    val profile = System.getenv("QUARKUS_PROFILE") ?: "dev"
    systemProperty("quarkus.profile", profile)
    jvmArgs("--enable-native-access=ALL-UNNAMED")
}

tasks.withType<Test> {
    useJUnitPlatform()
}
