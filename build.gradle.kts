plugins {
  //trick: for the same plugin versions in all sub-modules
  id("com.android.application") version "7.3.1" apply false
  id("com.android.library") version "7.3.1" apply false
  kotlin("android") version "1.8.0" apply false
  kotlin("multiplatform") version "1.8.0" apply false
  id("org.jetbrains.kotlin.jvm") version "1.8.0" apply false
}

buildscript {
  dependencies {
    classpath("org.jetbrains.compose:compose-gradle-plugin:1.3.0")
    classpath("org.jetbrains.kotlin:kotlin-serialization:1.8.10")
    classpath("app.cash.molecule:molecule-gradle-plugin:0.7.1")
    classpath("com.codingfeline.buildkonfig:buildkonfig-gradle-plugin:0.13.3")
  }
}

tasks.register("clean", Delete::class) {
  delete(rootProject.buildDir)
}
