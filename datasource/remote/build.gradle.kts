plugins {
  alias(libs.plugins.android.library)
  alias(libs.plugins.kotlin.multiplatform)
  alias(libs.plugins.kotlin.serialization)
}

kotlin {
  androidTarget()
  jvm()
  sourceSets {
    commonMain {
      dependencies {
        implementation(projects.timber)
        implementation(projects.common)
        implementation(projects.model)
        implementation(libs.ktor.client)
        implementation(libs.kotlin.serialization)
        implementation(libs.ktor.client.content.negotiation)
        implementation(libs.ktor.serialization.json)
      }
    }
    jvmTest {
      dependencies {
        implementation(libs.ktor.client.mock)
        implementation(libs.kotlin.test)
        implementation(libs.kotlin.test.junit)
        implementation(libs.kotlin.coroutine.test)
        implementation(libs.mockk)
        implementation(libs.turbine)
      }
    }
  }
}

android {
  namespace = "io.github.castrokingjames.datasource.remote"
  compileSdk = 34
  defaultConfig {
    minSdk = 24
  }

  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
  }
}
