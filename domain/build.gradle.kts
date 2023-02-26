tasks.test {
    useJUnitPlatform()
}

dependencies {
    api("dev.ustits.krefty:krefty-core:0.3.4")
    implementation(libs.kotlinx.coroutinesCore)

    testImplementation(libs.kotest.runnerJUnit5)
    testImplementation(libs.kotest.assertionsCore)
    testImplementation(libs.kotest.extensionsAssertionsKtor)

    testImplementation(libs.mockk.mockk)
}