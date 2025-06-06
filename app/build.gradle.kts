import org.jetbrains.compose.desktop.application.dsl.TargetFormat.Deb
import org.jetbrains.compose.desktop.application.dsl.TargetFormat.Dmg
import org.jetbrains.compose.desktop.application.dsl.TargetFormat.Msi
import org.jetbrains.kotlin.gradle.targets.js.dsl.ExperimentalWasmDsl

plugins {
  kotlin("multiplatform")
  alias(libs.plugins.android.application)
  alias(libs.plugins.compose.multiplatform)
  alias(libs.plugins.kotlin.compose)
  alias(libs.plugins.kotlin.serialization)
}

kotlin {
  applyDefaultHierarchyTemplate()

  androidTarget {
    compilations.all {
      kotlinOptions {
        jvmTarget = JavaVersion.VERSION_17.getMajorVersion()
      }
    }
  }

  listOf(
    iosX64("uikitX64"),
    iosArm64("uikitArm64"),
    iosSimulatorArm64("uikitSimulatorArm64"),
  ).forEach {
    it.binaries{
      framework {
        baseName = "app"

        // Only need this if you wish to add your own AppDelegate in swift
        export(project(":decompose-router"))
      }

      executable {
        entryPoint = "io.github.xxfast.decompose.router.app.main"
        freeCompilerArgs += listOf(
          "-linker-option", "-framework", "-linker-option", "Metal",
          "-linker-option", "-framework", "-linker-option", "CoreText",
          "-linker-option", "-framework", "-linker-option", "CoreGraphics"
        )
      }
    }
  }

  jvm("desktop") {
    compilations.all {
      kotlinOptions {
        jvmTarget = JavaVersion.VERSION_17.getMajorVersion()
      }
    }
  }

  js(IR) {
    browser()
    binaries.executable()
  }

  @OptIn(ExperimentalWasmDsl::class)
  wasmJs {
    browser()
    binaries.executable()
  }

  sourceSets {
    val commonMain by getting {
      dependencies {
        // Only need to add this as api if you wish to add your own AppDelegate in swift
        api(project(":decompose-router"))

        implementation(compose.runtime)
        implementation(compose.foundation)
        implementation(compose.material3)
        implementation(compose.materialIconsExtended)
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

    val desktopMain by getting {
      dependencies {
        implementation(compose.desktop.currentOs)
        implementation(libs.decompose.compose)
        implementation(libs.kotlinx.coroutines.swing)
      }
    }
  }
}

android {
  namespace = "io.github.xxfast.decompose.router.app"
  compileSdk = 35
  defaultConfig {
    minSdk = 25
    targetSdk = 35
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

compose.desktop {
  application {
    mainClass = "io.github.xxfast.decompose.router.app.ApplicationKt"

    nativeDistributions {
      targetFormats(Dmg, Msi, Deb)

      packageName = "App"
      packageVersion = "1.0.0"
    }
  }
}

compose.experimental {
  web.application { }
}
