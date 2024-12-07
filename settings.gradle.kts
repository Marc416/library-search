plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.5.0"
}
rootProject.name = "library-search"
include("search-api")
include("external")
include("common")
