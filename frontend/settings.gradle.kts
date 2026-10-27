pluginManagement {
    repositories {
        google()
        mavenCentral()
        // AÑADIMOS ESTA LÍNEA - El repositorio principal para plugins de Gradle
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "Supletanes"
include(":app")