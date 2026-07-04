# Renetik Android Testing

[![Android Build](https://github.com/renetik/renetik-android-testing/actions/workflows/android.yml/badge.svg)](https://github.com/renetik/renetik-android-testing/actions/workflows/android.yml)

Shared Android test dependencies and helpers for Renetik Android libraries.

## Installation

Add JitPack to dependency resolution:

```gradle
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven { url 'https://jitpack.io' }
    }
}
```

Add the testing artifact to test dependencies:

```gradle
dependencies {
    testImplementation 'com.github.renetik:renetik-android-testing:1.0.1'
}
```

## Compatibility

- Java: 21
- Gradle wrapper: 9.5.0
- Android Gradle Plugin: 9.2.1
- Kotlin: 2.3.21
- compileSdk: 36
- minSdk: 26

## Included Test Dependencies

- JUnit 4
- MockK
- Robolectric
- kotlinx-coroutines-test

## Release

Commit all intended release changes, make sure the `master` working tree is clean, then pass the new version to the release script:

```sh
./release.sh 1.0.1
```

The script validates the version and repository state, builds and publishes the release locally with the requested coordinates, pushes `master`, and creates the matching GitHub release and tag. JitPack then builds that tag on demand.
