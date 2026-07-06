[![Android Build](https://github.com/renetik/renetik-android/actions/workflows/android.yml/badge.svg)](https://github.com/renetik/renetik-android/actions/workflows/android.yml)

## Renetik Android UI

Part of [Renetik Android](https://github.com/renetik/renetik-android/).

View classes and Kotlin extensions for building Android UI with less
boilerplate. This module is the UI foundation for the Controller, Material,
Imaging, and UI Picker modules.

- **CS view classes**: `CSLinearLayout`, `CSFrameLayout`,
  `CSConstraintLayout`, `CSEditText`, `CSTextView`, and related views.
- **View extensions**: visibility, dimensions, margins, positions, animation,
  touch handling, tags, model storage, and selection/active state helpers.
- **Widget extensions**: `TextView`, `EditText`, `ImageView`, pickers, scroll
  views, compound buttons, seek bars, radio groups, and factories.
- **Graphics/protocols**: `CSImaging`, drawable helpers, `CSViewInterface`, and
  `CSVisibility`.

### Installation

Add JitPack and the UI dependency:

```gradle
repositories {
    google()
    mavenCentral()
    maven { url = 'https://jitpack.io' }
}

dependencies {
    implementation 'com.github.renetik.renetik-android:renetik-android-ui:2.0.1'
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
import renetik.android.ui.view.gone
import renetik.android.ui.view.shownIf
import renetik.android.ui.view.size
import renetik.android.ui.widget.text

val isLoading = property(false)

view.gone()
view.shownIf(isLoading)
view.size(width = 200, height = 100)
textView.text("Ready")
```

See the [sample app](../sample); its checklist screen is built with these UI
extensions.

### Core concepts

- **`renetik.android.ui.view`**: CS view classes and core `View`/`ViewGroup`
  extensions.
- **`renetik.android.ui.widget`**: widget-specific extensions and layout
  factories.
- **`renetik.android.ui.graphics`**: `CSImaging`, `CSAndroidImaging`, and bitmap
  or drawable helpers.
- **`renetik.android.ui.protocol`**: uniform protocols used by the controller
  layer.

### Dependencies

- **Renetik modules**: [Core](../renetik-android-core),
  [Event](../renetik-android-event).
- **External runtime**: AndroidX ConstraintLayout `2.2.1`, AndroidX
  AsyncLayoutInflater `1.1.0`.
- **Test scope**: [Testing](../renetik-android-testing).

### Related Renetik libraries

- [Renetik Android](https://github.com/renetik/renetik-android/)
- [Renetik Android Controller](../renetik-android-controller)
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
