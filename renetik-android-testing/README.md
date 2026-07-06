[![Android Build](https://github.com/renetik/renetik-android-framework/actions/workflows/android.yml/badge.svg)](https://github.com/renetik/renetik-android-framework/actions/workflows/android.yml)

# Renetik Android Testing

Shared Android test dependencies and helpers for Renetik Android libraries.
Part of [Renetik Android Framework](https://github.com/renetik/renetik-android-framework/).

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
    testImplementation 'com.github.renetik.renetik-android-framework:renetik-android-testing:2.0'
}
```

Use `master-SNAPSHOT` instead of `2.0` to test the latest framework `master`.

## Compatibility

- Java: 21
- Gradle wrapper: 9.5.0
- Android Gradle Plugin: 9.2.1
- Kotlin: 2.3.21
- compileSdk: 37
- minSdk: 26

## Included Test Dependencies

- JUnit 4
- MockK
- Robolectric
- kotlinx-coroutines-test

## Renetik Android Libraries

See [Renetik Android Framework](https://github.com/renetik/renetik-android-framework/) for all modules and release coordinates.
