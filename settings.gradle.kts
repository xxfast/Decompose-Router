pluginManagement {
  repositories {
    google()
    gradlePluginPortal()
    mavenCentral()
  }
}

dependencyResolutionManagement {
  repositories {
    google()
    mavenCentral()
  }
}

rootProject.name = "Decompose-Router"
include(":decompose-router")
include(":decompose-router-wear")
include(":app")
