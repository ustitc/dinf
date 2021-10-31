import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    `java-library`
    kotlin("jvm")
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile>() {
    kotlinOptions.jvmTarget = "11"
}

dependencies {
    implementation(project(":domain"))
    implementation(kotlin("stdlib-jdk8"))

    runtimeOnly("org.postgresql:postgresql:42.3.1")
    implementation("com.zaxxer:HikariCP:5.0.0")
    implementation(libs.exposed.core)
    implementation(libs.exposed.jdbc)
    implementation(libs.exposed.javatime)
    implementation(libs.exposed.dao)

    testImplementation(libs.kotest.runnerJUnit5)
    testImplementation(libs.kotest.assertionsCore)
    testImplementation("io.kotest.extensions:kotest-extensions-testcontainers:1.0.1")

    testImplementation(libs.testContainers.testcontainers)
    testImplementation(libs.testContainers.junitJupiter)
    testImplementation(libs.testContainers.postgresql)
}
