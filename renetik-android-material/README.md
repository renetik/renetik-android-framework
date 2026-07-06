[![Android Build](https://github.com/renetik/renetik-android/actions/workflows/android.yml/badge.svg)](https://github.com/renetik/renetik-android/actions/workflows/android.yml)

## Renetik Android Material

Part of [Renetik Android](https://github.com/renetik/renetik-android/).

Material Components integration for Renetik Android UI and controller code:
sliders, form helpers, dialogs, chips, snackbar, and progress extensions.
Requires a Material Components theme.

- **Slider views**: `CSSlider`, `CSRangeSlider`, and `CSVerticalSlider`.
- **Two-way bindings**: bind `Slider` and `RangeSlider` values to
  `CSProperty` values.
- **Form helpers**: `TextInputLayout` and `EditText` extensions for icons,
  clear actions, and validation-friendly setup.
- **Material utilities**: dialog, chip group, snackbar, and progress indicator
  extensions.

### Installation

Add JitPack and the material dependency:

```gradle
repositories {
    google()
    mavenCentral()
    maven { url = 'https://jitpack.io' }
}

dependencies {
    implementation 'com.github.renetik.renetik-android:renetik-android-material:2.0.1'
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
import renetik.android.event.property.CSProperty.Companion.property
import renetik.android.material.value
import renetik.android.material.withStartIconClear

val volume = property(30)
slider.value(volume, min = 0, max = 100, step = 1)

editText.withStartIconClear { volume.value = 0 }
```

### Core concepts

- **`CSSlider` / `CSRangeSlider` / `CSVerticalSlider`**: Material slider views
  that cooperate with Renetik touch handling.
- **`Slider+CSMaterial` and `RangeSlider+CSMaterial`**: value setters, drag
  events, and `CSProperty` bindings.
- **`TextInputLayout+CSMaterial`**: form and icon helpers.
- **Controller helpers**: material dialog and snackbar extensions for
  `CSView`.

### Dependencies

- **Renetik modules**: [Core](../renetik-android-core),
  [Event](../renetik-android-event), [UI](../renetik-android-ui),
  [Controller](../renetik-android-controller).
- **External runtime**: Material Components `1.13.0`.
- **Test scope**: [Testing](../renetik-android-testing).

### Related Renetik libraries

- [Renetik Android](https://github.com/renetik/renetik-android/)
- [Renetik Android UI](../renetik-android-ui)
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
