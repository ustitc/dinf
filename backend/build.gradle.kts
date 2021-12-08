import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm")
    alias(libs.plugins.kotlin.serialization)
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile>() {
    kotlinOptions.jvmTarget = "11"
}

dependencies {
    implementation(project(":api"))
    implementation(project(":domain"))

    implementation(kotlin("stdlib-jdk8"))

    runtimeOnly("org.postgresql:postgresql:42.3.1")

    implementation("com.zaxxer:HikariCP:5.0.0")
    implementation(libs.exposed.core)
    implementation(libs.exposed.jdbc)
    implementation(libs.exposed.javatime)
    implementation(libs.exposed.dao)

    implementation(libs.ktor.clientCore)
    implementation(libs.ktor.clientCoreJVM)
    implementation(libs.ktor.clientApache)
    implementation(libs.ktor.serverCore)
    implementation(libs.ktor.serverHostCommon)
    implementation(libs.ktor.serverSessions)
    implementation(libs.ktor.serverNetty)
    implementation(libs.ktor.locations)
    implementation(libs.ktor.serialization)
    implementation(libs.ktor.webjars)
    implementation(libs.ktor.htmlBuilder)

    implementation("org.webjars.npm:bulma:0.9.3")

    implementation(libs.logback.classic)

    testImplementation(libs.ktor.serverTests)

    testImplementation(libs.kotest.runnerJUnit5)
    testImplementation(libs.kotest.assertionsCore)
    testImplementation(libs.kotest.extensionsAssertionsKtor)
    testImplementation(libs.kotest.extensionsTestcontainers)

    testImplementation(libs.testContainers.testcontainers)
    testImplementation(libs.testContainers.junitJupiter)
    testImplementation(libs.testContainers.postgresql)
}
