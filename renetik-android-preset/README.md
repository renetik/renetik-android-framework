[![Android Build](https://github.com/renetik/renetik-android/actions/workflows/android.yml/badge.svg)](https://github.com/renetik/renetik-android/actions/workflows/android.yml)

## Renetik Android Preset

Part of [Renetik Android](https://github.com/renetik/renetik-android/).

Named snapshots of store-backed properties. Build presets on top of the Store
module when an app needs user-selectable configurations such as instrument,
effect, layout, or performance settings.

- **Preset lifecycle**: load, reload, save-as-current, save to item, and delete
  item hooks.
- **Preset properties**: value and nullable variants for strings, numbers,
  booleans, IDs, lists, and JSON-backed data.
- **Nested presets**: child presets can save their state into a parent preset
  item.
- **Store context plumbing**: route property creation through active preset
  stores.

### Installation

Add JitPack and the preset dependency:

```gradle
repositories {
    google()
    mavenCentral()
    maven { url = 'https://jitpack.io' }
}

dependencies {
    implementation 'com.github.renetik.renetik-android:renetik-android-preset:2.0.1'
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

Your app supplies the `CSPresetItemList` and `CSPresetItem` implementations that
represent available preset items:

```kotlin
import renetik.android.event.lifecycle.CSModel
import renetik.android.preset.CSPreset
import renetik.android.preset.init
import renetik.android.preset.property
import renetik.android.store.type.CSJsonObjectStore

val owner = CSModel()
val preset = CSPreset(
    parent = owner,
    parentStore = CSJsonObjectStore(),
    key = "instrument",
    list = presetItems,
    notFoundItem = { fallbackPresetItem }
).init()

val tempo = preset.property(owner, "tempo", 120)
tempo.value = 128

preset.reloadInternal()  // restore values from the selected preset item
```

### Core concepts

- **`CSPreset`**: owns selected preset item, tracked properties, child presets,
  reload events, and save/delete events.
- **`CSPresetItem` / `CSPresetItemList`**: app-defined preset catalog and item
  storage.
- **`renetik.android.preset.property`**: preset-aware value, nullable, ID-list,
  and JSON property implementations.
- **`PresetStoreContext`**: routes store-backed properties through the active
  preset store.

### Dependencies

- **Renetik modules**: [Core](../renetik-android-core),
  [Event](../renetik-android-event), [JSON](../renetik-android-json),
  [Store](../renetik-android-store).
- **External runtime**: none beyond the shared Android/Kotlin stack.
- **Test scope**: [Testing](../renetik-android-testing).

### Related Renetik libraries

- [Renetik Android](https://github.com/renetik/renetik-android/)
- [Renetik Android Store](../renetik-android-store)
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
