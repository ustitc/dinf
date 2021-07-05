plugins {
    `java-library`
}

tasks.test {
    useJUnitPlatform()
}

val exposedVersion: String = "0.32.1"
val kotestVersion: String = "4.6.0"
val testContainersVersion: String = "1.15.3"

dependencies {
    implementation(project(":domain:data"))

    runtimeOnly("org.postgresql:postgresql:42.2.19")
    implementation("com.zaxxer:HikariCP:4.0.3")
    implementation("org.jetbrains.exposed:exposed-core:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-jdbc:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-java-time:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-dao:$exposedVersion")

    testImplementation("io.kotest:kotest-runner-junit5:$kotestVersion")
    testImplementation("io.kotest:kotest-assertions-core:$kotestVersion")
    testImplementation("io.kotest.extensions:kotest-assertions-arrow:1.0.2")
    testImplementation("io.kotest.extensions:kotest-extensions-testcontainers:1.0.0")

    testImplementation("org.testcontainers:testcontainers:$testContainersVersion")
    testImplementation("org.testcontainers:junit-jupiter:$testContainersVersion")
    testImplementation("org.testcontainers:postgresql:$testContainersVersion")
}
