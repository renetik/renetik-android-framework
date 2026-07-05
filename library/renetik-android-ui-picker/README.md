[![Android Build](https://github.com/renetik/renetik-android-framework/actions/workflows/android.yml/badge.svg)](https://github.com/renetik/renetik-android-framework/actions/workflows/android.yml)

## Renetik Android UI Picker

Part of [Renetik Android Framework](https://github.com/renetik/renetik-android-framework/).

Number/value picker wheel built on [ShawnLin013/NumberPicker](https://github.com/ShawnLin013/NumberPicker).

- **`CSNumberPicker`**: picker wheel exposing selection as a `CSProperty<Int>` (`index`) and a scroll event.
- **Extensions**: `load(data, selected)` to display any list, `index(property)` two-way binding, `min`/`max`/`circulate`/`disableTextEditing`.

### Installation

```gradle
repositories {
    google()
    mavenCentral()
    maven { url = 'https://jitpack.io' }
}

dependencies {
    implementation 'com.github.renetik.renetik-android-framework:renetik-android-ui-picker:2.0'
}
```

Use `master-SNAPSHOT` instead of `2.0` to test the latest framework `master`.

### Compatibility
- **minSdk**: 26
- **target/compileSdk**: 37
- **Kotlin**: 2.3.21
- **AGP**: 9.2.1

### Quick start

```kotlin
import renetik.android.ui.picker.CSNumberPicker
import renetik.android.ui.picker.index
import renetik.android.ui.picker.load

val picker = CSNumberPicker(context)
picker.load(listOf("one", "two", "three"), selected = 1)

val selection = property(0)
picker.index(selection)          // two-way binding
```

### License
This library is released under the terms of the license in `LICENSE.txt`.
