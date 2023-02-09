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
        implementation(compose.desktop.currentOs)
        implementation(compose.runtime)
        implementation(compose.foundation)
        implementation(compose.material3)
        implementation(compose.materialIconsExtended)
        implementation(compose.preview)
        implementation("com.arkivanov.decompose:decompose:1.0.0")
        implementation("com.arkivanov.decompose:extensions-compose-jetbrains:1.0.0")
        implementation("app.cash.molecule:molecule-runtime:0.7.0")
      }
    }

    val commonTest by getting {
      dependencies {
        implementation(kotlin("test"))
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
