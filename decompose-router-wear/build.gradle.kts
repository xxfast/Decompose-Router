import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
  alias(libs.plugins.kotlin.multiplatform)
  alias(libs.plugins.android.kotlin.multiplatform.library)
  alias(libs.plugins.compose.multiplatform)
  alias(libs.plugins.kotlin.compose)
}

kotlin {
  android {
    namespace = "io.github.xxfast.decompose.router.wear"
    compileSdk = 36
    minSdk = 25

    compilerOptions {
      jvmTarget.set(JvmTarget.JVM_17)
    }
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
