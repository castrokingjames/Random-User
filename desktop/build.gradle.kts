import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
  alias(libs.plugins.kotlin.multiplatform)
  alias(libs.plugins.compose)
  alias(libs.plugins.compose.compiler)
}

kotlin {
  jvm()
  sourceSets {
    commonMain {
      dependencies {
        implementation(projects.core)
      }
    }
    jvmMain {
      dependencies {
        implementation(compose.desktop.currentOs)
        implementation(libs.logback)
        implementation(libs.kotlin.coroutines.swing)
      }
    }
  }
}

compose.desktop {
  application {
    mainClass = "io.github.castrokingjames.randomuser.desktop.Application_desktopKt"

    nativeDistributions {
      targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
      packageName = "io.github.castrokingjames.randomuser.desktop"
      packageVersion = "1.0.0"
    }
  }
}
