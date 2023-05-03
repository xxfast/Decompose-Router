@file:OptIn(org.jetbrains.compose.ExperimentalComposeLibrary::class)

plugins {
  kotlin("multiplatform")
  id("com.android.library")
  id("org.jetbrains.compose")
  id("kotlin-parcelize")
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
        implementation(libs.decompose)
        implementation(libs.decompose.compose.multiplatform)
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
  namespace = "io.github.xxfast.decompose.router"
  compileSdk = 33
  defaultConfig {
    minSdk = 24
    targetSdk = 33
  }
}

// TODO: Remove once a compiler with support for >1.8.21 available
compose {
  kotlinCompilerPlugin.set(dependencies.compiler.forKotlin("1.8.20"))
  kotlinCompilerPluginArgs.add("suppressKotlinVersionCompatibilityCheck=1.8.21")
}
