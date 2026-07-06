[![Android Build](https://github.com/renetik/renetik-android/actions/workflows/android.yml/badge.svg)](https://github.com/renetik/renetik-android/actions/workflows/android.yml)

## Renetik Android Core LeakCanary

Part of [Renetik Android](https://github.com/renetik/renetik-android/).

LeakCanary wiring for the Renetik Android `CSLeakCanary` facade in
`renetik-android-core`. Add this artifact to debug builds only; the facade
stays a no-op in release builds.

- **Automatic install**: `CSLeakCanaryProvider` installs the implementation from
  the debug APK without application code.
- **Facade integration**: `CSLeakCanaryImplementation` backs the core
  `CSLeakCanary` API.
- **Renetik object labels**: leak traces can include `id`, `key`, `name`, and
  `title` values from Renetik objects.
- **Test safety**: the implementation is a no-op under test runners.

### Installation

Add JitPack and the debug dependency:

```gradle
repositories {
    google()
    mavenCentral()
    maven { url = 'https://jitpack.io' }
}

dependencies {
    debugImplementation 'com.github.renetik.renetik-android:renetik-android-core-leakcanary:2.0.1'
}
```

Use `master-SNAPSHOT` instead of `2.0.1` to test the latest Renetik Android
`master`.

### Compatibility

- **minSdk**: 26
- **compileSdk**: 37
- **Java**: 21
- **Kotlin**: 2.3.21
- **AGP**: 9.2.1

### Quick start

No application setup is required when the artifact is present in a debug build.
Use the facade from core when you need to control leak detection:

```kotlin
import renetik.android.core.lang.CSLeakCanary

CSLeakCanary.disable()
CSLeakCanary.enable()
with(CSLeakCanary) { screen.expectWeaklyReachable { "released screen" } }
```

### Core concepts

- **`CSLeakCanaryProvider`**: content provider that installs the implementation
  automatically.
- **`CSLeakCanaryImplementation`**: enables/disables heap dumping and connects
  LeakCanary to Renetik object metadata.
- **`CSLeakCanary`**: facade declared in core and implemented by this debug-only
  module.

### Dependencies

- **Renetik modules**: [Core](../renetik-android-core).
- **External runtime**: LeakCanary `2.14`.
- **Test scope**: [Testing](../renetik-android-testing).

### Related Renetik libraries

- [Renetik Android](https://github.com/renetik/renetik-android/)
- [Renetik Android Core](../renetik-android-core)
- [Renetik Android Framework](../renetik-android-framework)

### Contributing

Issues and PRs are welcome. Please include a clear description and small,
focused changes.

### License

This library is released under the terms of [LICENSE.txt](../LICENSE.txt).

---

If you find this useful, consider supporting the broader Renetik Instruments
effort at [renetik.com](https://www.renetik.com) or get in touch for
[Hire](https://renetik.github.io).
