plugins {
  alias(libs.plugins.kover)
}

dependencies {
  kover(projects.model)
  kover(projects.domain)
  kover(projects.data)
  kover(projects.datasource.remote)
  kover(projects.datasource.local)
  kover(projects.feature.user.list)
  kover(projects.feature.user.details)
}

kover.reports {
  filters {
    excludes.classes(
      "io.github.castrokingjames.datasource.local.database.*",
      "*ComposableSingletons*",
    )
    excludes.annotatedBy(
      "kotlinx.serialization.Serializable",
    )
  }
}
