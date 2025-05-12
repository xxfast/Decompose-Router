import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.targets.js.dsl.ExperimentalWasmDsl

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

  js(IR) {
    browser()
  }

  @OptIn(ExperimentalWasmDsl::class)
  wasmJs {
    browser()
  }

  sourceSets {
    val commonMain by getting {
      dependencies {
        api(libs.decompose)
        api(libs.decompose.compose)

        implementation(compose.runtime)
        implementation(compose.foundation)
        implementation(libs.kotlinx.serialization.core)
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

    val androidMain by getting {
      dependencies {
        implementation(compose.material3)
        implementation(libs.decompose)
        implementation(libs.decompose.compose)
        implementation(libs.androidx.activity.ktx)
        implementation(libs.androidx.activity.compose)
        implementation(libs.androidx.fragment.ktx)
      }
    }

    val androidInstrumentedTest by getting {
      dependencies {
        implementation(libs.compose.ui.junit4)
      }
    }

    @OptIn(ExperimentalKotlinGradlePluginApi::class)
    sourceSets.invokeWhenCreated("androidDebug") {
      dependencies {
        implementation(libs.compose.ui.test.manifest)
      }
    }

    val jsMain by getting {
      dependencies {
        implementation("org.jetbrains.kotlin-wrappers:kotlin-browser:1.0.0-pre.752")
      }
    }
  }
}

android {
  namespace = "io.github.xxfast.decompose.router"
  compileSdk = 35
  defaultConfig {
    minSdk = 25
    targetSdk = 35
    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
  }

  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
  }

  testOptions {
    unitTests {
      isIncludeAndroidResources = true
    }
  }
}
