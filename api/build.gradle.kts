plugins {
    kotlin("multiplatform")
    alias(libs.plugins.kotlin.serialization)
}

kotlin {
    jvm()
    js(IR) {
        nodejs()
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(kotlin("stdlib-common"))
                api("org.jetbrains.kotlinx:kotlinx-datetime:0.2.1")
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.0")
            }
        }
        val commonTest by getting
        val jvmMain by getting
        val jvmTest by getting
        val jsMain by getting
        val jsTest by getting
    }
}
