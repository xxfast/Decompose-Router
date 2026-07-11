plugins {
  alias(libs.plugins.android.application)
}

android {
  namespace = "io.github.xxfast.decompose.router.androidApp"
  compileSdk = 36

  defaultConfig {
    applicationId = "io.github.xxfast.decompose.router.app"
    minSdk = 25
    targetSdk = 36
    versionCode = 1
    versionName = "1.0"
  }

  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
  }
}

dependencies {
  implementation(project(":app"))
}
