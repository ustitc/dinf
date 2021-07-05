plugins {
    `java-library`
}

tasks.test {
    useJUnitPlatform()
}

dependencies {
    api("io.arrow-kt:arrow-refined-types:1.5.0-SNAPSHOT")
}