<h1 align="center">Random User Compose Multiplatform</h1>

<p align="center">
  <a href="https://opensource.org/licenses/Apache-2.0"><img alt="License" src="https://img.shields.io/badge/License-Apache%202.0-blue.svg"/></a>
  <a href="https://android-arsenal.com/api?level=24"><img alt="API" src="https://img.shields.io/badge/API-24%2B-brightgreen.svg?style=flat"/></a>
  <a href="https://github.com/castrokingjames/random-user/actions"><img alt="Build Status" src="https://github.com/castrokingjames/random-user/actions/workflows/build.yaml/badge.svg"/></a> <br>
</p>

<p align="center">  
A Random User API implementation made with Kotlin and Compose Multiplatform, Koin, Coroutines, Flow, Jetpack (Sqldelight, Decompose), and Material Design based on MVVM architecture.
</p>

<p align="center">
<img src="https://raw.githubusercontent.com/castrokingjames/random-user/refs/heads/gh-pages/assets/screenshot.jpg"/>
</p>

## Download
Go to the [Releases](https://github.com/castrokingjames/random-user/releases) to download the latest APK.

## Tech stack & Open-source libraries
- Minimum SDK level 24.
- Architecture:
  - MVVM Architecture (View - ViewModel - Model): Facilitates separation of concerns and promotes maintainability.
  - Repository Pattern: Acts as a mediator between different data sources and the application's business logic.
- [Kotlin](https://kotlinlang.org/) based, utilizing [Coroutines](https://github.com/Kotlin/kotlinx.coroutines) + [Flow](https://kotlin.github.io/kotlinx.coroutines/kotlinx-coroutines-core/kotlinx.coroutines.flow/) for asynchronous operations.
- [Compose Multiplatform](https://www.jetbrains.com/compose-multiplatform): Kotlin Compose Multiplatform UI Framework.
- [Koin](https://insert-koin.io): Kotlin Multiplatform Dependency Injection framework.
- [Ktor](https://ktor.io/): Kotlin Multiplatform asynchronous HTTP client,
- [Kotlin Serialization](https://github.com/Kotlin/kotlinx.serialization): Kotlin multiplatform / multi-format reflectionless serialization.
- [Turbine](https://github.com/cashapp/turbine): A small testing library for kotlinx.coroutines Flow.
- [SQLDelight](https://github.com/cashapp/sqldelight): Generates typesafe Kotlin APIs from SQL.
- [Decompose](https://arkivanov.github.io/Decompose): Kotlin Multiplatform library for breaking down your code into lifecycle-aware business logic components (aka BLoC).
  - [Navigation](https://arkivanov.github.io/Decompose/navigation/overview): Decompose navigation API
- [Baseline Profiles](https://medium.com/proandroiddev/improve-your-android-app-performance-with-baseline-profiles-297f388082e6): Enhances app performance by including specifications of classes and methods in the APK that can be utilized by Android Runtime.

## Architecture
**Random User Compose** adheres to the MVVM architecture and implements the Repository pattern, aligning with [Google's official architecture guidance](https://developer.android.com/topic/architecture).

![architecture](https://raw.githubusercontent.com/castrokingjames/random-user/refs/heads/gh-pages/assets/figure0.png)

The architecture of **Random User Compose** is structured into two distinct layers: the UI layer and the data layer. Each layer fulfills specific roles and responsibilities, outlined as follows:

**Random User Compose** follows the principles outlined in the [Guide to app architecture](https://developer.android.com/topic/architecture), making it an exemplary demonstration of architectural concepts in practical application.

### Architecture Overview

![architecture](https://raw.githubusercontent.com/castrokingjames/random-user/refs/heads/gh-pages/assets/figure1.png)

- Each layer adheres to the principles of [unidirectional event/data flow](https://developer.android.com/topic/architecture/ui-layer#udf): the UI layer sends user events to the data layer, and the data layer provides data streams to other layers.
- The data layer operates autonomously from other layers, maintaining purity without dependencies on external layers.

This loosely coupled architecture enhances component reusability and app scalability, facilitating seamless development and maintenance.

### UI Layer

![architecture](https://raw.githubusercontent.com/castrokingjames/random-user/refs/heads/gh-pages/assets/figure2.png)

The UI layer encompasses UI elements responsible for configuring screens for user interaction, alongside the [ViewModel/Component](https://arkivanov.github.io/Decompose/component/overview), which manages app states and restores data during configuration changes.
- UI elements observe the data flow, ensuring synchronization with the underlying data layer.

### Data Layer

![architecture](https://raw.githubusercontent.com/castrokingjames/random-user/refs/heads/gh-pages/assets/figure3.png)

The data layer is composed of repositories that handle business logic tasks such as retrieving data from a local database or fetching remote data from a network. This layer is designed to prioritize offline access, functioning primarily as an offline-first repository of business logic. It adheres to the principle of "single source of truth," ensuring that all data operations are centralized and consistent.<br>

**Random User Compose** is an offline-first app, meaning it can perform all or most of its essential functions without an internet connection. This design allows users to access core features reliably, regardless of network availability, reducing their need for constant updates and decreasing data usage. For more details on how to build an offline-first application, you can visit [Build an offline-first app](https://developer.android.com/topic/architecture/data-layer/offline-first).

## Modularization

![architecture](https://raw.githubusercontent.com/castrokingjames/random-user/refs/heads/gh-pages/assets/figure4.png)

**Random User Compose** adopted modularization strategies below:

- **Reusability**: Modulizing reusable codes properly enable opportunities for code sharing and limits code accessibility in other modules at the same time.
- **Parallel Building**: Each module can be run in parallel and it reduces the build time.
- **Strict visibility control**: Modules restrict to expose dedicated components and access to other layers, so it prevents they're being used outside the module
- **Decentralized focusing**: Each developer team can assign their dedicated module and they can focus on their own modules.

For more information, check out the [Guide to Android app modularization](https://developer.android.com/topic/modularization).

## Open API
Random User Compose using the [Random User API](https://randomuser.me/) for constructing RESTful API.<br>
