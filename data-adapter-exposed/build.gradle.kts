plugins {
    `java-library`
}

tasks.test {
    useJUnitPlatform()
}

val exposedVersion: String = "0.32.1"

dependencies {
    testImplementation("io.mockk:mockk:1.12.0")
    implementation(project(":domain-data"))
    implementation("org.jetbrains.exposed:exposed-core:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-jdbc:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-java-time:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-dao:$exposedVersion")
}