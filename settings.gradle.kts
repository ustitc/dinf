pluginManagement {
    repositories {
        gradlePluginPortal()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }
}

enableFeaturePreview("VERSION_CATALOGS")

rootProject.name = "dinf"

include("backend")
include("libs:htmx")
include("libs:hyperscript")
include("libs:ktor-ext")
