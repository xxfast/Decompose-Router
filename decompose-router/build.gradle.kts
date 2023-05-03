@file:OptIn(org.jetbrains.compose.ExperimentalComposeLibrary::class)

plugins {
  kotlin("multiplatform")
  id("com.android.library")
  id("org.jetbrains.compose")
  id("kotlin-parcelize")
  id("app.cash.molecule")
}

kotlin {
  android()

  iosX64()
  iosArm64()
  iosSimulatorArm64()

  jvm("desktop") {
    compilations.all {
      kotlinOptions {
        jvmTarget = "15"
      }
    }
  }

  sourceSets {
    val commonMain by getting {
      dependencies {
        implementation(compose.runtime)
        implementation(compose.foundation)
        implementation(compose.material3)
        implementation(compose.materialIconsExtended)
        implementation("com.arkivanov.decompose:decompose:1.0.0-compose-experimental")
        implementation("com.arkivanov.decompose:extensions-compose-jetbrains:1.0.0-compose-experimental")
        implementation("app.cash.molecule:molecule-runtime:0.7.1")
      }
    }

    val commonTest by getting {
      dependencies {
        implementation(kotlin("test"))
      }
    }

    val iosX64Main by getting
    val iosArm64Main by getting
    val iosSimulatorArm64Main by getting
    val iosMain by creating {
      dependsOn(commonMain)
      iosX64Main.dependsOn(this)
      iosArm64Main.dependsOn(this)
      iosSimulatorArm64Main.dependsOn(this)
      dependencies {
      }
    }
  }
}

android {
  namespace = "io.github.xxfast.krouter"
  compileSdk = 33
  defaultConfig {
    minSdk = 24
    targetSdk = 33
  }
}
