import org.jetbrains.compose.desktop.application.dsl.TargetFormat.Deb
import org.jetbrains.compose.desktop.application.dsl.TargetFormat.Dmg
import org.jetbrains.compose.desktop.application.dsl.TargetFormat.Msi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl

plugins {
  alias(libs.plugins.kotlin.multiplatform)
  alias(libs.plugins.android.kotlin.multiplatform.library)
  alias(libs.plugins.compose.multiplatform)
  alias(libs.plugins.kotlin.compose)
  alias(libs.plugins.kotlin.serialization)
}

kotlin {
  applyDefaultHierarchyTemplate()

  android {
    namespace = "io.github.xxfast.decompose.router.app"
    compileSdk = 36
    minSdk = 25

    // Workaround for CMP-9547: with the new com.android.kotlin.multiplatform.library
    // plugin (AGP 9), the Compose resources copy task for the test variants is left
    // with no configured outputDirectory and fails. Enabling android resources wires it up.
    androidResources.enable = true

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

  listOf(
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
    compilerOptions {
      jvmTarget.set(JvmTarget.JVM_17)
    }
  }

  js {
    browser()
    binaries.executable()
  }

  @OptIn(ExperimentalWasmDsl::class)
  wasmJs {
    browser()
    binaries.executable()
  }

  sourceSets {
    commonMain.dependencies {
      // Only need to add this as api if you wish to add your own AppDelegate in swift
      api(project(":decompose-router"))

      implementation(compose.runtime)
      implementation(compose.foundation)
      implementation(compose.material3)
      implementation(compose.materialIconsExtended)
    }

    commonTest.dependencies {
      implementation(kotlin("test"))
    }

    androidMain.dependencies {
      implementation(libs.androidx.activity.ktx)
      implementation(libs.androidx.activity.compose)
      implementation(libs.androidx.fragment.ktx)
    }

    named("androidDeviceTest").dependencies {
      implementation(libs.compose.ui.junit4)
      implementation(libs.compose.ui.test.manifest)
      // compose-ui-test pulls in espresso-core 3.5.0 transitively, whose reflective
      // InputManager.getInstance() call throws NoSuchMethodException on newer Android
      // (API 35+). 3.7.0 switches to getSystemService and fixes instrumented tests.
      implementation(libs.espresso.core)
    }

    named("desktopMain").dependencies {
      implementation(compose.desktop.currentOs)
      implementation(libs.decompose.compose)
      implementation(libs.kotlinx.coroutines.swing)
    }
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
