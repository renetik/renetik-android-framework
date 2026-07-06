[![Android Build](https://github.com/renetik/renetik-android/actions/workflows/android.yml/badge.svg)](https://github.com/renetik/renetik-android/actions/workflows/android.yml)

## Renetik Android Core

Part of [Renetik Android](https://github.com/renetik/renetik-android/).

Kotlin and Android foundation for the Renetik Android modules: logging,
language primitives, collection helpers, math and geometry, colors, file and
content helpers, and lazy/atomic/variable primitives. Every runtime module
depends on core.

- **Language primitives**: `CSLazyVal`, `CSLazyVar`, `CSVariable`, atomics,
  tuples, results, guards, and runtime helpers.
- **Kotlin/Java extensions**: compact helpers for common types, collections,
  strings, files, and reflection.
- **Android helpers**: context, resources, display, handler, graphics, file, and
  shared-preferences helpers.
- **Logging**: `CSLog`, `CSAndroidLogger`, `CSPrintLogger`, and listener support.
- **Leak facade**: `CSLeakCanary` facade implemented by the optional
  Core LeakCanary module.

### Installation

Add JitPack and the core dependency:

```gradle
repositories {
    google()
    mavenCentral()
    maven { url = 'https://jitpack.io' }
}

dependencies {
    implementation 'com.github.renetik.renetik-android:renetik-android-core:2.0.1'
}
```

Use `master-SNAPSHOT` instead of `2.0.1` to test the latest Renetik Android
`master`.

Add LeakCanary wiring only to debug builds:

```gradle
dependencies {
    debugImplementation 'com.github.renetik.renetik-android:renetik-android-core-leakcanary:2.0.1'
}
```

### Compatibility

- **minSdk**: 26
- **compileSdk**: 37
- **Java**: 21
- **Kotlin**: 2.3.21
- **AGP**: 9.2.1

### Quick start

```kotlin
import renetik.android.core.kotlin.equalsAny
import renetik.android.core.lang.lazy.CSLazyVar.Companion.lazyVar
import renetik.android.core.logging.CSAndroidLogger
import renetik.android.core.logging.CSLog

var title by lazyVar { "Untitled" }
title = "Lead"

val isAudio = "wav".equalsAny("wav", "flac", "mp3")

CSLog.init(CSAndroidLogger(tag = "Player"), isTraceLineEnabled = true)
CSLog.logWarn { "audio format accepted: $isAudio" }
```

### Core concepts

- **`renetik.android.core.lang`**: core contracts, values, variables, lazy
  primitives, atomics, results, tuples, and `CSLeakCanary`.
- **`renetik.android.core.kotlin`**: Kotlin extensions for equality, casting,
  collections, strings, reflection, and small language helpers.
- **`renetik.android.core.java`**: Java/JDK file, stream, and concurrency
  helpers.
- **`renetik.android.core.android`**: Android platform helpers grouped by
  framework area.
- **`renetik.android.core.logging`**: logger abstraction and implementations.
- **`renetik.android.core.math`**: math and point helpers.

See [docs/package-map.md](docs/package-map.md) for a fuller package guide.

### Dependencies

- **Renetik modules**: none.
- **External runtime**: AndroidX Navigation Event `1.1.2`, plus the shared
  Kotlin/Android stack from `library.gradle`.
- **Test scope**: [Testing](../renetik-android-testing).

### Related Renetik libraries

- [Renetik Android](https://github.com/renetik/renetik-android/)
- [Renetik Android Core LeakCanary](../renetik-android-core-leakcanary)
- [Renetik Android Event](../renetik-android-event)
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
