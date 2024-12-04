plugins {
  kotlin("multiplatform")
  alias(libs.plugins.android.library)
  alias(libs.plugins.compose.multiplatform)
  alias(libs.plugins.kotlin.compose)
}

kotlin {
  androidTarget {
    publishLibraryVariants("release", "debug")
  }

  // TODO: Remove the phantom target after https://github.com/Kotlin/dokka/issues/3122
  jvm()

  sourceSets {
    val androidMain by getting {
      dependencies {
        api(project(":decompose-router"))

        implementation(compose.uiTooling)
        implementation(compose.materialIconsExtended)
        implementation(libs.horologist.compose.layouts)
        implementation(libs.wear.compose.foundation)
        implementation(libs.wear.compose.material)
        implementation(libs.wear.compose.ui.tooling)
        implementation(libs.androidx.activity.compose)
        implementation(libs.decompose)
        implementation(libs.decompose.compose)
      }
    }
  }
}

android {
  namespace = "io.github.xxfast.decompose.router.wear"
  compileSdk = 34
  defaultConfig {
    minSdk = 25
    targetSdk = 34
  }

  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
  }
}
