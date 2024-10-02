plugins {
  alias(libs.plugins.android.library)
  alias(libs.plugins.kotlin.multiplatform)
  alias(libs.plugins.sqldelight)
}

kotlin {
  androidTarget()
  jvm()
  sourceSets {
    commonMain {
      dependencies {
        api(libs.sqldelight.extension)
        implementation(projects.common)
        implementation(projects.model)
      }
    }
  }
}

android {
  namespace = "io.github.castrokingjames.datasource.local"
  compileSdk = 34
  defaultConfig {
    minSdk = 24
  }

  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
  }
}

sqldelight {
  databases {
    create("Database") {
      packageName = "io.github.castrokingjames.datasource.local.database"
      dialect("app.cash.sqldelight:sqlite-3-25-dialect:2.0.2")
      generateAsync = true
    }
  }
}
