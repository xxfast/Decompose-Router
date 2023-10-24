import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

plugins {
  id("org.jetbrains.dokka") version "1.8.20"
  id("org.jetbrains.kotlinx.binary-compatibility-validator") version "0.13.2"
}

buildscript {
  repositories {
    google()
    mavenCentral()
    gradlePluginPortal()
  }

  dependencies {
    classpath(libs.agp)
    classpath(libs.compose.multiplatform)
    classpath(libs.kotlin.gradle.plugin)
  }
}

apiValidation {
  ignoredProjects += listOf("app")
}

allprojects {
  repositories {
    google()
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    maven("https://s01.oss.sonatype.org/content/repositories/snapshots/")
  }

  group = "io.github.xxfast"
  version = "0.5.1"

  // Do not publish the app
  if (name.contains("app")) return@allprojects

  apply(plugin = "org.jetbrains.dokka")
  apply(plugin = "maven-publish")
  apply(plugin = "signing")

  extensions.configure<PublishingExtension> {
    repositories {
      maven {
        val isSnapshot = version.toString().endsWith("SNAPSHOT")
        url = uri(
          if (!isSnapshot) "https://s01.oss.sonatype.org/service/local/staging/deploy/maven2"
          else "https://s01.oss.sonatype.org/content/repositories/snapshots"
        )

        credentials {
          username = gradleLocalProperties(rootDir).getProperty("sonatypeUsername")
          password = gradleLocalProperties(rootDir).getProperty("sonatypePassword")
        }
      }
    }

    val javadocJar = tasks.register<Jar>("javadocJar") {
      dependsOn(tasks.dokkaHtml)
      archiveClassifier.set("javadoc")
      from("$buildDir/dokka")
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
      gradleLocalProperties(rootDir).getProperty("gpgKeySecret"),
      gradleLocalProperties(rootDir).getProperty("gpgKeyPassword"),
    )

    sign(publishing.publications)
  }

  // TODO: remove after https://youtrack.jetbrains.com/issue/KT-46466 is fixed
  project.tasks.withType(AbstractPublishToMaven::class.java).configureEach {
    dependsOn(project.tasks.withType(Sign::class.java))
  }
}
