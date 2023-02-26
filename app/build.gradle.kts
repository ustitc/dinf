plugins {
    application
    alias(libs.plugins.kotlin.serialization)
}

application {
    mainClass.set("dinf.app.ApplicationKt")
}

tasks.test {
    useJUnitPlatform()
}

dependencies {
    implementation(project(":domain"))
    implementation(project(":libs:htmx"))
    implementation(project(":libs:hyperscript"))

    implementation("org.xerial:sqlite-jdbc:3.40.1.0")
    implementation("org.flywaydb:flyway-core:9.15.0")
    implementation("com.zaxxer:HikariCP:5.0.1")

    implementation(libs.ktor.clientCore)
    implementation(libs.ktor.clientCIO)
    implementation(libs.ktor.clientContentNegotiation)

    implementation(libs.ktor.serialization)
    implementation(libs.ktor.serializationJson)

    implementation(libs.ktor.serverCore)
    implementation(libs.ktor.serverHostCommon)
    implementation(libs.ktor.serverNetty)
    implementation(libs.ktor.serverResources)
    implementation(libs.ktor.serverWebjars)
    implementation(libs.ktor.serverHtmlBuilder)
    implementation(libs.ktor.serverStatusPages)
    implementation(libs.ktor.serverCallLogging)
    implementation(libs.ktor.serverMetricsMicrometer)
    implementation(libs.ktor.serverAuth)
    implementation(libs.ktor.serverSessions)

    implementation("io.micrometer:micrometer-registry-prometheus:1.10.4")

    implementation("org.json:json:20220320")
    implementation("org.mindrot:jbcrypt:0.4")

    implementation("org.webjars.npm:picocss__pico:1.5.7")
    implementation("org.webjars.npm:htmx.org:1.8.5")
    implementation("org.webjars.npm:hyperscript.org:0.9.7")

    implementation("org.hashids:hashids:1.0.3")

    implementation(libs.hoplite.core)
    implementation(libs.hoplite.toml)

    implementation(libs.logback.classic)

    testImplementation(libs.ktor.serverTests)

    testImplementation(libs.kotest.runnerJUnit5)
    testImplementation(libs.kotest.assertionsCore)
    testImplementation(libs.kotest.extensionsAssertionsKtor)

    testImplementation(libs.mockk.mockk)
}
