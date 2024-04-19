pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven("https://jitpack.io")
        maven (  "https://storage.zego.im/maven" )   // <- Add this line.
        maven (  "https://jitpack.io" )
    }
}

rootProject.name = "JapanFigure"
include(":app")
