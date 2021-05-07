dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        mavenLocal()
        gradlePluginPortal()
        maven(url = "https://jitpack.io")
        maven(url = "https://plugins.gradle.org/m2/")
        maven(url = "https://dl.bintray.com/guardian/android")
    }
}
rootProject.name = "NetAloSDKAndroid"
include(":app")
 