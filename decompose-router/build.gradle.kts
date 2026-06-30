import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl

plugins {
  alias(libs.plugins.kotlin.multiplatform)
  alias(libs.plugins.android.kotlin.multiplatform.library)
  alias(libs.plugins.compose.multiplatform)
  alias(libs.plugins.kotlin.compose)
}

kotlin {
  android {
    namespace = "io.github.xxfast.decompose.router"
    compileSdk = 36
    minSdk = 25

    withHostTestBuilder {}
    withDeviceTestBuilder {
      sourceSetTreeName = "test"
    }.configure {
      instrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    compilerOptions {
      jvmTarget.set(JvmTarget.JVM_17)
    }
  }

  iosArm64()
  iosSimulatorArm64()

  jvm("desktop") {
    compilerOptions {
      jvmTarget.set(JvmTarget.JVM_17)
    }
  }

  js {
    browser()
  }

  @OptIn(ExperimentalWasmDsl::class)
  wasmJs {
    browser()
  }

  sourceSets {
    commonMain.dependencies {
      api(libs.decompose)
      api(libs.decompose.compose)

      implementation(compose.runtime)
      implementation(compose.foundation)
      implementation(libs.kotlinx.serialization.core)
    }

    commonTest.dependencies {
      implementation(kotlin("test"))
    }

    androidMain.dependencies {
      implementation(compose.material3)
      implementation(libs.decompose)
      implementation(libs.decompose.compose)
      implementation(libs.androidx.activity.ktx)
      implementation(libs.androidx.activity.compose)
      implementation(libs.androidx.fragment.ktx)
    }

    named("androidDeviceTest").dependencies {
      implementation(libs.compose.ui.junit4)
      implementation(libs.compose.ui.test.manifest)
    }

    jsMain.dependencies {
      implementation("org.jetbrains.kotlin-wrappers:kotlin-browser:1.0.0-pre.752")
    }
  }
}
