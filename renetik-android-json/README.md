[![Android Build](https://github.com/renetik/renetik-android/actions/workflows/android.yml/badge.svg)](https://github.com/renetik/renetik-android/actions/workflows/android.yml)

## Renetik Android JSON

Part of [Renetik Android](https://github.com/renetik/renetik-android/).

JSON mapping helpers for Kotlin objects, lists, and maps, with a lightweight
`CSJsonObject` model used by Store and Preset modules.

- **Object model**: `CSJsonObject` stores mutable JSON-backed data with typed
  getters and setters.
- **Serialization helpers**: `toJson`, `parseJson`, `parseJsonMap`, and
  `parseJsonList`.
- **Round-trip loading**: load JSON strings, files, assets, maps, and lists into
  Renetik JSON objects.
- **Formatting options**: pretty printing and optional force-string output via
  `CSJson`.

### Installation

Add JitPack and the JSON dependency:

```gradle
repositories {
    google()
    mavenCentral()
    maven { url = 'https://jitpack.io' }
}

dependencies {
    implementation 'com.github.renetik.renetik-android:renetik-android-json:2.0.1'
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
import renetik.android.json.obj.CSJsonObject
import renetik.android.json.obj.load
import renetik.android.json.toJson

class TrackJson(
    var title: String? = null,
    var gain: Double = 1.0
) : CSJsonObject() {

    init {
        set("title", title)
        set("gain", gain)
    }

    override fun onLoad() {
        title = getString("title")
        gain = getDouble("gain", default = 1.0)
    }
}

val json = TrackJson("Lead", gain = 0.75).toJson(formatted = true)
val restored = TrackJson().load(json)
```

### Core concepts

- **`CSJsonObject`**: mutable JSON object with typed getters/setters and
  `onLoad()` hook.
- **`CSJsonArray`**: mutable JSON array wrapper.
- **Conversion extensions**: map/list/object conversion to JSON strings and
  `org.json` types.
- **`CSJson`**: global JSON options such as `forceString` and pretty printing.

### Dependencies

- **Renetik modules**: [Core](../renetik-android-core).
- **External runtime**: none beyond the shared Android/Kotlin stack.
- **Test scope**: [Testing](../renetik-android-testing).

### Related Renetik libraries

- [Renetik Android](https://github.com/renetik/renetik-android/)
- [Renetik Android Store](../renetik-android-store)
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
