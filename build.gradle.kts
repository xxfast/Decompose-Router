plugins {
  alias(libs.plugins.kotlin.multiplatform) apply false
  alias(libs.plugins.android.kotlin.multiplatform.library) apply false
  alias(libs.plugins.kotlin.compose) apply false
  alias(libs.plugins.compose.multiplatform) apply false
  alias(libs.plugins.maven.publish) apply false

  alias(libs.plugins.dokka)
  alias(libs.plugins.binary.compatibility.validator)
}

buildscript {
  repositories {
    google()
    mavenCentral()
    gradlePluginPortal()
  }
}

apiValidation {
  ignoredProjects += listOf("app")
}

// Aggregate API documentation from the published modules into a single site
dependencies {
  dokka(project(":decompose-router"))
  dokka(project(":decompose-router-wear"))
}

allprojects {
  repositories {
    google()
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
  }

  group = "io.github.xxfast"
  version = "0.10.0-SNAPSHOT"
}

subprojects {
  // The demo app is not published and has no API docs.
  if (name == "app") return@subprojects

  apply(plugin = "org.jetbrains.dokka")
  apply(plugin = "com.vanniktech.maven.publish")

  // Publishes to the Sonatype Central Portal (central.sonatype.com). Credentials/signing
  // come from Gradle properties: mavenCentralUsername/mavenCentralPassword and
  // signingInMemoryKey/signingInMemoryKeyPassword (see CI env / ~/.gradle/gradle.properties).
  configure<com.vanniktech.maven.publish.MavenPublishBaseExtension> {
    publishToMavenCentral() // no automaticRelease: deployment waits for manual "Publish" on the portal
    signAllPublications()

    pom {
      name.set("Decompose-Router")
      description.set("A Compose-multiplatform navigation library that leverage Decompose to create an API inspired by Conductor")
      url.set("https://github.com/xxfast/Decompose-Router")
      licenses {
        license {
          name.set("Apache-2.0")
          url.set("https://opensource.org/licenses/Apache-2.0")
        }
      }
      issueManagement {
        system.set("Github")
        url.set("https://github.com/xxfast/Decompose-Router/issues")
      }
      scm {
        url.set("https://github.com/xxfast/Decompose-Router")
        connection.set("scm:git:git://github.com/xxfast/Decompose-Router.git")
        developerConnection.set("scm:git:ssh://git@github.com/xxfast/Decompose-Router.git")
      }
      developers {
        developer {
          id.set("xxfast")
          name.set("Isuru Rajapakse")
          email.set("isurukusumal36@gmail.com")
        }
      }
    }
  }

  // Secondary target: GitHub Packages. Maven Central (above) stays the primary registry.
  // Publish with `./gradlew publishAllPublicationsToGitHubPackagesRepository`. In CI the
  // credentials come from the auto-provided GITHUB_ACTOR / GITHUB_TOKEN env vars.
  configure<PublishingExtension> {
    repositories {
      maven {
        name = "GitHubPackages"
        url = uri("https://maven.pkg.github.com/xxfast/Decompose-Router")
        credentials {
          username = System.getenv("GITHUB_ACTOR")
          password = System.getenv("GITHUB_TOKEN")
        }
      }
    }
  }
}
