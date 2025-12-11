plugins {
    kotlin("jvm") version "2.2.21"
}

group = "by.n3b"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.arrow-kt:arrow-core:2.2.0")
    implementation("org.ojalgo:ojalgo:55.0.0")

    testImplementation("org.junit.jupiter:junit-jupiter-api:6.0.1")
    testImplementation("org.assertj:assertj-core:3.27.6")

    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:6.0.1")
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}
