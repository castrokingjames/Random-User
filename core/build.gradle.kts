@file:OptIn(ExperimentalKotlinGradlePluginApi::class)

import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi

plugins {
  alias(libs.plugins.android.library)
  alias(libs.plugins.kotlin.multiplatform)
  alias(libs.plugins.kotlin.serialization)
  alias(libs.plugins.compose)
  alias(libs.plugins.compose.compiler)
}

kotlin {
  androidTarget()
  jvm()
  sourceSets {
    commonMain {
      dependencies {
        api(compose.components.resources)
        api(libs.koin)
        api(libs.decompose)
        api(libs.decompose.extensions)
        api(libs.essenty.lifecycle)
        api(libs.essenty.statekeeper)
        api(libs.essenty.instancekeeper)
        api(libs.essenty.backhandler)
        api(libs.essenty.lifecycle.coroutines)
        api(libs.ktor.client)
        api(libs.ktor.client.logging)
        api(libs.ktor.client.content.negotiation)
        api(libs.ktor.serialization.json)
        api(libs.kotlin.coroutines)
        api(libs.coil)
        api(libs.coil.ktor)
        api(projects.common)
        api(projects.timber)
        api(projects.designsystem)
        api(projects.model)
        api(projects.domain)
        api(projects.data)
        api(projects.datasource.remote)
        api(projects.datasource.local)
        api(projects.feature.user.list)
        api(projects.feature.user.details)
      }
    }
    androidMain {
      dependencies {
        api(libs.ktor.client.okhttp)
        api(libs.sqldelight.android)
      }
    }
    jvmMain {
      dependencies {
        api(libs.ktor.client.okhttp)
        api(libs.sqldelight.jdbc)
      }
    }
  }

  // https://youtrack.jetbrains.com/issue/KT-61573
  compilerOptions {
    freeCompilerArgs.add("-Xexpect-actual-classes")
  }
}

android {
  namespace = "io.github.castrokingjames"
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
