plugins {
    `java-library`
}

tasks.test {
    useJUnitPlatform()
}

dependencies {
    api(project(":domain:types"))
    api(project(":domain:data"))
    api("io.arrow-kt:arrow-core:0.13.2")
}