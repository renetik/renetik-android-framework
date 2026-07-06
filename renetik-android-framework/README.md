[![Android Build](https://github.com/renetik/renetik-android-framework/actions/workflows/android.yml/badge.svg)](https://github.com/renetik/renetik-android-framework/actions/workflows/android.yml)

## Renetik Android Framework (aggregate)

Part of [Renetik Android Framework](https://github.com/renetik/renetik-android-framework/).

Aggregate artifact that pulls in every runtime module of the framework as an
`api` dependency — one line instead of eleven: core, event, json, store,
preset, ui, ui-picker, material, imaging and controller. It contains no code
of its own.

### Installation

```gradle
repositories {
    google()
    mavenCentral()
    maven { url = 'https://jitpack.io' }
}

dependencies {
    implementation 'com.github.renetik.renetik-android-framework:renetik-android-framework:2.0'
    debugImplementation 'com.github.renetik.renetik-android-framework:renetik-android-core-leakcanary:2.0'
}
```

Use `master-SNAPSHOT` instead of `2.0` to test the latest framework `master`.
Pick individual modules instead if you want a smaller dependency footprint —
see the [module matrix](../README.md#modules).

### Compatibility
- **minSdk**: 26
- **target/compileSdk**: 37
- **Kotlin**: 2.3.21
- **AGP**: 9.2.1

### License
This library is released under the terms of the license in `LICENSE.txt`.
