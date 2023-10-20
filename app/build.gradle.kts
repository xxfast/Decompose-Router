import org.jetbrains.compose.desktop.application.dsl.TargetFormat.Deb
import org.jetbrains.compose.desktop.application.dsl.TargetFormat.Dmg
import org.jetbrains.compose.desktop.application.dsl.TargetFormat.Msi
import org.jetbrains.compose.experimental.dsl.IOSDevices

plugins {
  kotlin("multiplatform")
  id("com.android.application")
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

  sourceSets {
    val commonMain by getting {
      dependencies {
        // Only need to add this as api if you wish to add your own AppDelegate in swift
        api(project(":decompose-router"))

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

    val desktopMain by getting {
      dependencies {
        implementation(compose.desktop.currentOs)
        implementation(libs.decompose.compose.multiplatform)
        implementation(libs.kotlinx.coroutines.swing)
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
  web.application {
  }

  uikit.application {
    bundleIdPrefix = "io.github.xxfast.decompose.router.app"
    projectName = "DecomposeRouterApp"

    deployConfigurations {
      simulator("IPhone12Pro") {
        // Usage: ./gradlew iosDeployIPhone12ProDebug
        device = IOSDevices.IPHONE_12_PRO
      }

      /*
       * Usage: ./gradlew iosDeployDeviceDebug
       *
       * If your are getting "A valid provisioning profile for this executable was not found" error,
       * see: https://developer.apple.com/forums/thread/128121?answerId=403323022#403323022
       */
      connectedDevice("Device") {
        // Uncomment and fill with your Team ID
        // teamId = ""
      }
    }
  }
}
