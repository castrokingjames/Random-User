import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi

plugins {
  alias(libs.plugins.android.library)
  alias(libs.plugins.kotlin.multiplatform)
  alias(libs.plugins.compose)
  alias(libs.plugins.compose.compiler)
}

kotlin {
  androidTarget()
  jvm()
  sourceSets {
    commonMain {
      dependencies {
        implementation(libs.decompose)
        implementation(libs.essenty.lifecycle)
        implementation(libs.essenty.statekeeper)
        implementation(libs.essenty.instancekeeper)
        implementation(libs.essenty.backhandler)
        implementation(libs.essenty.lifecycle.coroutines)
        implementation(libs.kotlin.coroutines)
        implementation(libs.coil)
        implementation(projects.timber)
        implementation(projects.common)
        implementation(projects.designsystem)
        implementation(projects.model)
        implementation(projects.domain)
      }
    }
    jvmTest {
      dependencies {
        implementation(libs.kotlin.test)
        implementation(libs.kotlin.test.junit)
        implementation(libs.kotlin.coroutine.test)
        implementation(libs.mockk)
        implementation(libs.turbine)
      }
    }
  }

  // https://youtrack.jetbrains.com/issue/KT-61573
  @OptIn(ExperimentalKotlinGradlePluginApi::class)
  compilerOptions {
    freeCompilerArgs.add("-Xexpect-actual-classes")
  }
}

android {
  namespace = "io.github.castrokingjames.feature.user.list"
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
