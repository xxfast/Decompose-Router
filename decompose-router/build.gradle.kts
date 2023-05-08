import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi

plugins {
  kotlin("multiplatform")
  id("com.android.library")
  id("org.jetbrains.compose")
  id("kotlin-parcelize")
}

kotlin {
  android {
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

  sourceSets {
    val commonMain by getting {
      dependencies {
        implementation(compose.runtime)
        implementation(compose.foundation)
        implementation(libs.essenty.parcelable)
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

    val jsMain by getting

    val androidMain by getting {
      dependencies {
        implementation(compose.material3)
        implementation(libs.decompose)
        implementation(libs.decompose.compose.multiplatform)
        implementation(libs.androidx.activity.ktx)
        implementation(libs.androidx.activity.compose)
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
  }
}

android {
  namespace = "io.github.xxfast.decompose.router"
  compileSdk = 33
  defaultConfig {
    minSdk = 24
    targetSdk = 33
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

// TODO: Remove once a compiler with support for >1.8.21 available
compose {
  kotlinCompilerPlugin.set(dependencies.compiler.forKotlin("1.8.20"))
  kotlinCompilerPluginArgs.add("suppressKotlinVersionCompatibilityCheck=1.8.21")
}
