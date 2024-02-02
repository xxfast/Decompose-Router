#  Adding to your project

Decompose-Router is published on Maven Central
```kotlin
repositories {
  mavenCentral()
  // or for snapshot builds
  maven("https://s01.oss.sonatype.org/content/repositories/snapshots/")
}
```

Include the dependency in `commonMain`. Latest version [![Maven Central](https://img.shields.io/maven-central/v/io.github.xxfast/decompose-router?color=blue)](https://search.maven.org/search?q=g:io.github.xxfast)

> **Note** - Check for compatible versions of Compose Multiplatform, Decompose and Essenty in the [Version Catalog]

## With version catalog

```toml
[versions]
# Check in gradle/libs.versions.toml

[libraries]
# For Compose Multiplatform
decompose-router = { module = "io.github.xxfast:decompose-router", version.ref = "decompose-router" }

# For Compose Wear
decompose-router-wear = { module = "io.github.xxfast:decompose-router-wear", version.ref = "decompose-router" }

# You will need to also bring in decompose extensions for compose-multiplatform
decompose-compose-multiplatform = { module = "com.arkivanov.decompose:extensions-compose-jetbrains", version.ref = "decompose" }
```

**build.gradle.kts**
```kotlin
sourceSets {
// For Compose Multiplatform
val commonMain by getting { 
  dependencies { 
    implementation(libs.decompose.router)
    
    // You will probably need to also bring in decompose and essenty
    implementation(libs.decompose)
    implementation(libs.decompose.compose.multiplatform)
    implementation(libs.essenty.parcelable)
  } 
}

// For Compose Wear
val androidMain by getting {
  dependencies { 
    implementation(libs.decompose.router.wear)
  } 
}
}
```

## Without version catalog

**build.gradle.kts**
```kotlin
sourceSets {
    // For Compose Multiplatform
    val commonMain by getting {
        dependencies {
            implementation("io.github.xxfast:decompose-router:${versions.decompose - router}")

            // You will need to also bring in decompose and essenty
            implementation("com.arkivanov.decompose:decompose:${versions.decompose}")
            implementation("com.arkivanov.decompose:extensions-compose-jetbrains:${versions.decompose}")
            implementation("com.arkivanov.essenty:parcelable:${versions.essenty}")
        }
    }

    // For Compose Wear
    val androidMain by getting {
        dependencies {
            implementation("io.github.xxfast:decompose-router-wear:${versions.decompose - router}")
        }
    }
}
```
