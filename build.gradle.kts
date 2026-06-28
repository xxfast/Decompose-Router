import java.util.Properties

plugins {
  alias(libs.plugins.kotlin.multiplatform) apply false
  alias(libs.plugins.android.kotlin.multiplatform.library) apply false
  alias(libs.plugins.kotlin.compose) apply false
  alias(libs.plugins.compose.multiplatform) apply false

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

fun localProperties(): Properties = Properties().apply {
  val file = rootProject.file("local.properties")
  if (file.exists()) file.inputStream().use { load(it) }
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

  // Do not publish the app
  if (name.contains("app")) return@allprojects

  apply(plugin = "org.jetbrains.dokka")
  apply(plugin = "maven-publish")
  apply(plugin = "signing")

  val localProperties: Properties = localProperties()

  extensions.configure<PublishingExtension> {
    repositories {
      maven {
        val isSnapshot = version.toString().endsWith("SNAPSHOT")
        url = uri(
          if (!isSnapshot) "https://ossrh-staging-api.central.sonatype.com/service/local/staging/deploy/maven2"
          else "https://central.sonatype.com/repository/maven-snapshots/"
        )

        credentials {
          username = localProperties.getProperty("sonatypeUsername")
          password = localProperties.getProperty("sonatypePassword")
        }
      }
    }

    val javadocJar = tasks.register<Jar>("javadocJar") {
      dependsOn(tasks.named("dokkaGeneratePublicationHtml"))
      archiveClassifier.set("javadoc")
      from(layout.buildDirectory.dir("dokka/html"))
    }

    publications {
      withType<MavenPublication> {
        artifact(javadocJar)

        pom {
          name.set("Decompose-Router")
          description.set("A Compose-multiplatform navigation library that leverage Decompose to create an API inspired by Conductor")
          licenses {
            license {
              name.set("Apache-2.0")
              url.set("https://opensource.org/licenses/Apache-2.0")
            }
          }
          url.set("https://github.com/xxfast/Decompose-Router")
          issueManagement {
            system.set("Github")
            url.set("https://github.com/xxfast/Decompose-Router/issues")
          }
          scm {
            connection.set("https://github.com/xxfast/Decompose-Router.git")
            url.set("https://github.com/xxfast/Decompose-Router")
          }
          developers {
            developer {
              name.set("Isuru Rajapakse")
              email.set("isurukusumal36@gmail.com")
            }
          }
        }
      }
    }
  }

  val publishing = extensions.getByType<PublishingExtension>()
  extensions.configure<SigningExtension> {
    useInMemoryPgpKeys(
      localProperties.getProperty("gpgKeySecret"),
      localProperties.getProperty("gpgKeyPassword"),
    )

    sign(publishing.publications)
  }

  // TODO: remove after https://youtrack.jetbrains.com/issue/KT-46466 is fixed
  project.tasks.withType(AbstractPublishToMaven::class.java).configureEach {
    dependsOn(project.tasks.withType(Sign::class.java))
  }
}
