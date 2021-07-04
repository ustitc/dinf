import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.5.20" apply false
}

allprojects {

    apply {
        plugin("org.jetbrains.kotlin.jvm")
    }

    group = "me.ruslan"
    version = "1.0-SNAPSHOT"

    repositories {
        jcenter()
        mavenCentral()
        maven { url = uri("https://oss.sonatype.org/content/repositories/snapshots/") }
    }

    tasks.withType<KotlinCompile>() {
        kotlinOptions.jvmTarget = "11"
    }

}
