plugins { }

buildscript {
  repositories {
    mavenCentral()
    gradlePluginPortal()
    google()
  }

  dependencies {
    classpath(libs.agp)
    classpath(libs.kotlin.gradle.plugin)
    classpath(libs.compose.multiplatform)
  }
}

tasks.register("clean", Delete::class) {
  delete(rootProject.buildDir)
}
