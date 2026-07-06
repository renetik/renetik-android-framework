[![Android Build](https://github.com/renetik/renetik-android/actions/workflows/android.yml/badge.svg)](https://github.com/renetik/renetik-android/actions/workflows/android.yml)

## Renetik Android Framework

Part of [Renetik Android](https://github.com/renetik/renetik-android/).

Aggregate artifact that pulls in every Renetik Android runtime module as an
`api` dependency. It contains no code of its own; depend on it when you want the
whole framework in one line, or pick individual modules for a smaller footprint.

- **Foundation**: core, event, JSON, store, and preset modules.
- **UI stack**: UI, UI Picker, Material, Imaging, and Controller modules.
- **Single coordinate**: one dependency exposes the public runtime module graph.
- **Optional debug leaks**: pair with `renetik-android-core-leakcanary` in debug
  builds.

### Installation

Add JitPack and the aggregate dependency:

```gradle
repositories {
    google()
    mavenCentral()
    maven { url = 'https://jitpack.io' }
}

dependencies {
    implementation 'com.github.renetik.renetik-android:renetik-android-framework:2.0.1'
    debugImplementation 'com.github.renetik.renetik-android:renetik-android-core-leakcanary:2.0.1'
}
```

Use `master-SNAPSHOT` instead of `2.0.1` to test the latest Renetik Android
`master`. Pick individual modules instead if you want a smaller dependency
footprint; see the [module matrix](../README.md#modules).

### Compatibility

- **minSdk**: 26
- **compileSdk**: 37
- **Java**: 21
- **Kotlin**: 2.3.21
- **AGP**: 9.2.1

### Quick start

After adding the aggregate artifact, imports from each runtime module are
available:

```kotlin
import renetik.android.event.CSEvent.Companion.event
import renetik.android.store.property
import renetik.android.store.type.CSJsonObjectStore
import renetik.android.ui.view.gone

val onSaved = event<Unit>()
val store = CSJsonObjectStore()
var title: String by store.property("title", default = "Untitled")

view.gone()
onSaved.fire()
```

### Core concepts

- **Aggregate artifact**: depends on runtime modules with `api`, so consumers can
  import their public APIs.
- **No source code**: behavior lives in the individual modules.
- **Runtime-only scope**: test helpers remain in `renetik-android-testing` and
  `renetik-android-testing-ui`.

### Dependencies

- **Renetik modules**: Core, Event, JSON, Store, Preset, UI, UI Picker,
  Material, Imaging, and Controller.
- **External runtime**: transitive dependencies from the included modules.
- **Test scope**: [Testing](../renetik-android-testing).

### Related Renetik libraries

- [Renetik Android](https://github.com/renetik/renetik-android/)
- [Renetik Android Testing](../renetik-android-testing)
- [Renetik Android Testing UI](../renetik-android-testing-ui)

### Contributing

Issues and PRs are welcome. Please include a clear description and small,
focused changes.

### License

This library is released under the terms of [LICENSE.txt](../LICENSE.txt).

---

If you find this useful, consider supporting the broader Renetik Instruments
effort at [renetik.com](https://www.renetik.com) or get in touch for
[Hire](https://renetik.github.io).
