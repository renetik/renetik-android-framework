[![Android Build](https://github.com/renetik/renetik-android-framework/actions/workflows/android.yml/badge.svg)](https://github.com/renetik/renetik-android-framework/actions/workflows/android.yml)

## Renetik Android UI

Part of [Renetik Android Framework](https://github.com/renetik/renetik-android-framework/).

View classes and Kotlin extensions for building Android UI with less boilerplate.

- **`renetik.android.ui.view`**: CS view classes (`CSLinearLayout`, `CSFrameLayout`, `CSConstraintLayout`, `CSEditText`, …) and `View`/`ViewGroup` extensions for visibility, dimensions, margins, positions, animation and touch.
- **`renetik.android.ui.widget`**: `TextView`, `EditText`, `ImageView`, pickers, scroll views and other widget extensions, plus `LinearLayout` factories.
- **`renetik.android.ui.graphics`**: `CSImaging` abstraction with drawable/bitmap helpers (`CSAndroidImaging`, `CSTileDrawable`).
- **`renetik.android.ui.protocol`**: `CSViewInterface` and `CSVisibility` protocols with extensions used by the controller layer.

### Installation

```gradle
repositories {
    google()
    mavenCentral()
    maven { url = 'https://jitpack.io' }
}

dependencies {
    implementation 'com.github.renetik.renetik-android-framework:renetik-android-ui:2.0'
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
import renetik.android.ui.view.gone
import renetik.android.ui.view.shownIf
import renetik.android.ui.view.size
import renetik.android.ui.widget.text

view.gone()                       // visibility helpers: visible/invisible/gone
view.shownIf(isLoading)           // bind visibility to an observable property
view.size(width = 200, height = 100)
textView.text("Hello").textColorInt(color)
```

See the [sample app](../../sample) — its checklist screen is built with these extensions.

### License
This library is released under the terms of the license in `LICENSE.txt`.
