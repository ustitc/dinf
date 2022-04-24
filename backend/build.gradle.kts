plugins {
    application
    alias(libs.plugins.kotlin.serialization)
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

    implementation("org.xerial:sqlite-jdbc:3.36.0.3")
    implementation("org.flywaydb:flyway-core:8.5.5")
    implementation("com.zaxxer:HikariCP:5.0.1")

    implementation("com.meilisearch.sdk:meilisearch-java:0.7.0")

    implementation(libs.ktor.serverCore)
    implementation(libs.ktor.serverHostCommon)
    implementation(libs.ktor.serverNetty)
    implementation(libs.ktor.serialization)
    implementation(libs.ktor.serverResources)
    implementation(libs.ktor.serverWebjars)
    implementation(libs.ktor.serverHtmlBuilder)
    implementation(libs.ktor.serverStatusPages)
    implementation(libs.ktor.serverCallLogging)
    implementation(libs.ktor.serverMetricsMicrometer)

    implementation("io.micrometer:micrometer-registry-prometheus:1.8.4")

    implementation("org.json:json:20220320")

    implementation("org.webjars.bowergithub.picocss:pico:1.5.0")
    implementation("org.webjars.npm:htmx.org:1.7.0")
    implementation("org.webjars.npm:hyperscript.org:0.9.5")

    implementation("org.hashids:hashids:1.0.3")
    implementation("dev.ustits.krefty:krefty-core:0.3.4")

    implementation(libs.hoplite.core)
    implementation(libs.hoplite.toml)

    implementation(libs.logback.classic)

    testImplementation(libs.ktor.serverTests)

    testImplementation(libs.kotest.runnerJUnit5)
    testImplementation(libs.kotest.assertionsCore)
    testImplementation(libs.kotest.extensionsAssertionsKtor)
}
