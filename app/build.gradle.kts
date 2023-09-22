plugins {
  kotlin("multiplatform")
  id("com.android.library")
  id("org.jetbrains.compose")
  id("kotlin-parcelize")
}

@OptIn(org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi::class)
kotlin {
  targetHierarchy.default()

  androidTarget {
    compilations.all {
      kotlinOptions {
        jvmTarget = JavaVersion.VERSION_17.getMajorVersion()
      }
    }
  }

  listOf(
    iosX64(),
    iosArm64(),
    iosSimulatorArm64()
  ).forEach {
    it.binaries.framework {
      baseName = "app"
    }
  }

  sourceSets {
    val commonMain by getting {
      dependencies {
        implementation(project(":decompose-router"))

        implementation(compose.runtime)
        implementation(compose.foundation)
        implementation(compose.material3)

        implementation(libs.decompose)
        implementation(libs.decompose.compose.multiplatform)
        implementation(libs.essenty.parcelable)
      }
    }

    val commonTest by getting {
      dependencies {
        implementation(kotlin("test"))
      }
    }

    val androidMain by getting {
      dependencies {
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
  }
}

android {
  namespace = "io.github.xxfast.decompose.router.app"
  compileSdk = 34
  defaultConfig {
    minSdk = 21
    targetSdk = 34
    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
  }

  buildFeatures {
    compose = true
  }

  composeOptions {
    kotlinCompilerExtensionVersion = "1.5.2"
  }

  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
  }
}
