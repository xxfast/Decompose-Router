plugins {
  kotlin("multiplatform")
  id("com.android.library")
  id("org.jetbrains.compose")
}

kotlin {
  android {
    publishLibraryVariants("release", "debug")
  }

  sourceSets {
    val commonMain by getting {
      dependencies {
        implementation(project(":decompose-router"))
        implementation(compose.uiTooling)
        implementation(compose.materialIconsExtended)
        implementation(libs.horologist.compose.layouts)
        implementation(libs.wear.compose.foundation)
        implementation(libs.wear.compose.material)
        implementation(libs.wear.compose.ui.tooling)
        implementation(libs.androidx.activity.compose)
        implementation(libs.essenty.parcelable)
        implementation(libs.decompose)
        implementation(libs.decompose.compose.multiplatform)
      }
    }
  }
}

android {
  namespace = "io.github.xxfast.decompose.router.wear"
  compileSdk = 34
  defaultConfig {
    minSdk = 21
    targetSdk = 34
  }

  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
  }
}

// TODO: Remove once a compiler with support for >1.8.21 available
compose {
  kotlinCompilerPlugin.set(dependencies.compiler.forKotlin("1.8.20"))
  kotlinCompilerPluginArgs.add("suppressKotlinVersionCompatibilityCheck=1.8.22")
}
