[![Android Build](https://github.com/renetik/renetik-android-framework/actions/workflows/android.yml/badge.svg)](https://github.com/renetik/renetik-android-framework/actions/workflows/android.yml)

## Renetik Android Material

Part of [Renetik Android Framework](https://github.com/renetik/renetik-android-framework/).

Material Components integration: CS slider views and extensions binding Material widgets to framework properties.

- **`CSSlider`, `CSRangeSlider`, `CSVerticalSlider`**: Material sliders usable inside CS touch handling (`CSMaterialSlider`).
- **`Slider`/`RangeSlider` extensions**: `value(property, min, max, step)` two-way binding, `onChange`, `onDragStart/Stop`, clamped `value(...)` setters.
- **Form helpers**: `EditText.inputLayout`, `withStartIcon`, `withEndIcon`, `withStartIconClear`, `withStartClearText` for `TextInputLayout` forms.
- **Dialogs, chips, snackbar, progress**: `MaterialAlertDialogBuilder`, `ChipGroup`, `Snackbar` and `LinearProgressIndicator` extensions.

### Installation

```gradle
repositories {
    google()
    mavenCentral()
    maven { url = 'https://jitpack.io' }
}

dependencies {
    implementation 'com.github.renetik.renetik-android-framework:renetik-android-material:2.0'
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
import renetik.android.material.value

// two-way bind a slider to a property, values rounded to step
val volume = property(30)
slider.value(volume, min = 0, max = 100, step = 1)

// TextInputLayout form helpers
editText.withStartIconClear { /* cleared */ }
```

Requires a Material Components theme (see the [sample app](../../sample)).

### License
This library is released under the terms of the license in `LICENSE.txt`.
