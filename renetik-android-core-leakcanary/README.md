[![Android Build](https://github.com/renetik/renetik-android/actions/workflows/android.yml/badge.svg)](https://github.com/renetik/renetik-android/actions/workflows/android.yml)

## Renetik Android Core LeakCanary

Part of [Renetik Android](https://github.com/renetik/renetik-android/).

[LeakCanary](https://square.github.io/leakcanary/) wiring for the Renetik Android `CSLeakCanary` facade in `renetik-android-core`.

- **`CSLeakCanaryProvider`**: content provider that installs the implementation automatically — no code needed.
- **`CSLeakCanaryImplementation`**: enables/disables heap dumping, labels leaks with `id`/`key`/`name`/`title` fields of Renetik objects, and is a safe no-op under test runners.

### Installation

Add it to debug builds only; the `CSLeakCanary` facade in core stays a no-op in release:

```gradle
repositories {
    google()
    mavenCentral()
    maven { url = 'https://jitpack.io' }
}

dependencies {
    debugImplementation 'com.github.renetik.renetik-android:renetik-android-core-leakcanary:2.0'
}
```

Use `master-SNAPSHOT` instead of `2.0` to test the latest Renetik Android `master`.

### Compatibility
- **minSdk**: 26
- **target/compileSdk**: 37
- **Kotlin**: 2.3.21
- **AGP**: 9.2.1

### Usage

```kotlin
import renetik.android.core.lang.CSLeakCanary

CSLeakCanary.disable()      // e.g. during stress tests
CSLeakCanary.enable()
with(CSLeakCanary) { someObject.expectWeaklyReachable { "released screen" } }
```

### License
This library is released under the terms of the license in `LICENSE.txt`.
