[![Android Build](https://github.com/renetik/renetik-android/actions/workflows/android.yml/badge.svg)](https://github.com/renetik/renetik-android/actions/workflows/android.yml)

## Renetik Android Store

Part of [Renetik Android](https://github.com/renetik/renetik-android/).

Key-value stores with typed, observable property delegates. Back a store with
JSON, a string, a file, an Android `Bundle`, or `SharedPreferences`, then read
and write values through `CSProperty`-style delegates that survive reloads.

- **Store implementations**: JSON object, string JSON, file JSON, simple file
  JSON, bundle JSON, preferences, and preferences JSON stores.
- **Typed delegates**: value, nullable, late, list item, ID-list, JSON object,
  JSON list, and data-property variants.
- **Observable reloads**: delegates update when the backing store reloads and
  can fire change callbacks.
- **Store contexts**: reusable context APIs for creating store-backed
  properties in models/controllers.

### Installation

Add JitPack and the store dependency:

```gradle
repositories {
    google()
    mavenCentral()
    maven { url = 'https://jitpack.io' }
}

dependencies {
    implementation 'com.github.renetik.renetik-android:renetik-android-store:2.0.1'
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

```kotlin
import renetik.android.store.property
import renetik.android.store.reload
import renetik.android.store.type.CSStringJsonStore

val store = CSStringJsonStore()

var title: String by store.property("title", default = "Untitled")
var gain: Float by store.property("gain", default = 1f)

title = "Lead"
gain = 0.75f
println(store.jsonString) // {"title":"Lead","gain":0.75}

store.reload("""{"title":"Bass","gain":0.5}""")
println(title) // Bass
```

JSON-backed nested data:

```kotlin
import renetik.android.store.property
import renetik.android.store.type.CSJsonObjectStore

class TrackSettings : CSJsonObjectStore() {
    var title: String by property("title", default = "Untitled")
    var gain: Float by property("gain", default = 1f)
}

val root = CSJsonObjectStore()
val track: TrackSettings by root.property("track")
track.title = "Lead"
```

### Core concepts

- **`CSStore`**: common key-value store contract with load/reload/change events.
- **Store types**: `CSJsonObjectStore`, `CSStringJsonStore`,
  `CSFileJsonStore`, `CSSimpleFileJsonStore`, `CSBundleJsonStore`,
  `CSPreferencesStore`, and `CSPreferencesJsonStore`.
- **Property factories**: `property`, `dataProperty`, `late*Property`,
  `null*Property`, JSON property, JSON list property, and list item helpers.
- **`CSStoreContext`**: model-friendly abstraction for creating store-backed
  values from a shared store.

### Dependencies

- **Renetik modules**: [Core](../renetik-android-core),
  [Event](../renetik-android-event), [JSON](../renetik-android-json).
- **External runtime**: none beyond the shared Android/Kotlin stack.
- **Test scope**: [Testing](../renetik-android-testing).

### Related Renetik libraries

- [Renetik Android](https://github.com/renetik/renetik-android/)
- [Renetik Android JSON](../renetik-android-json)
- [Renetik Android Preset](../renetik-android-preset)
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
