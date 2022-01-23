plugins {
    kotlin("multiplatform")
}

kotlin {
    jvm()
    js(IR) {
        nodejs()
        browser()
        binaries.executable()
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(kotlin("stdlib-common"))
                implementation(libs.kotlinx.coroutinesCore)
                api("org.jetbrains.kotlinx:kotlinx-datetime:0.2.1")
                api("dev.ustits.krefty:krefty-core:0.3.4")
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(libs.kotest.frameworkEngine)
                implementation(libs.kotest.assertionsCore)
                implementation(libs.kotest.property)
            }
        }
        val jvmMain by getting
        val jvmTest by getting
        val jsMain by getting {
            dependencies {
                implementation(kotlin("stdlib-js"))
            }
        }
        val jsTest by getting
    }
}
