import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

buildscript {
    repositories {
        mavenCentral()
    }

    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.7.20")
    }
}

group = "dinf"
version = "1.0-SNAPSHOT"

allprojects {
    apply(plugin = "kotlin")

    tasks.withType<KotlinCompile>() {
        kotlinOptions.jvmTarget = "17"
    }

    repositories {
        mavenCentral()
        mavenLocal()
    }
}
