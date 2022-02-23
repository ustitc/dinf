plugins {
    alias(libs.plugins.kotlin.serialization)
    application
}

application {
    mainClass.set("dinf.ApplicationKt")
}

tasks.test {
    useJUnitPlatform()
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation(project(":libs:htmx"))
    implementation(project(":libs:hyperscript"))
    implementation(project(":libs:ktor-ext"))

    runtimeOnly("org.postgresql:postgresql:42.3.1")
    implementation("org.xerial:sqlite-jdbc:3.36.0.3")

    implementation("com.zaxxer:HikariCP:5.0.1")

    implementation("com.meilisearch.sdk:meilisearch-java:0.7.0")

    implementation(libs.exposed.core)
    implementation(libs.exposed.jdbc)
    implementation(libs.exposed.javatime)
    implementation(libs.exposed.dao)

    implementation(libs.ktor.serverCore)
    implementation(libs.ktor.serverHostCommon)
    implementation(libs.ktor.serverNetty)
    implementation(libs.ktor.locations)
    implementation(libs.ktor.serialization)
    implementation(libs.ktor.webjars)
    implementation(libs.ktor.htmlBuilder)
    implementation(libs.ktor.metricsMicrometer)

    implementation("io.micrometer:micrometer-registry-prometheus:1.8.2")

    implementation("org.webjars.bowergithub.picocss:pico:1.4.2")
    implementation("org.webjars.npm:htmx.org:1.6.1")
    implementation("org.webjars.npm:hyperscript.org:0.9.4")

    implementation("org.hashids:hashids:1.0.3")
    implementation("dev.ustits.krefty:krefty-core:0.3.4")

    implementation(libs.hoplite.core)
    implementation(libs.hoplite.toml)

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
