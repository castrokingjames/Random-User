plugins {
  alias(libs.plugins.android.library)
  alias(libs.plugins.kotlin.multiplatform)
  alias(libs.plugins.compose)
  alias(libs.plugins.compose.compiler)
}

compose.resources {
  publicResClass = true
  packageOfResClass = "io.github.castrokingjames"
  generateResClass = auto
}

kotlin {
  androidTarget()
  jvm()
  sourceSets {
    commonMain {
      dependencies {
        api(compose.runtime)
        api(compose.components.resources)
        api(compose.material)
        api(compose.material3)
        api(compose.materialIconsExtended)
        api(compose.foundation)
        api(libs.coil)
        implementation(projects.timber)
      }
    }
    androidMain {
      dependencies {
        api(libs.compose.activity)
        api(libs.androidx.window)
      }
    }
  }
}

android {
  namespace = "io.github.castrokingjames.ui.compose"
  compileSdk = 34
  defaultConfig {
    minSdk = 24
  }

  composeOptions {
    kotlinCompilerExtensionVersion = libs.versions.compose.compiler.get()
  }

  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
  }
}
