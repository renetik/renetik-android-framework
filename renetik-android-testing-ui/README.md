[![Android Build](https://github.com/renetik/renetik-android/actions/workflows/android.yml/badge.svg)](https://github.com/renetik/renetik-android/actions/workflows/android.yml)

## Renetik Android Testing UI

Part of [Renetik Android](https://github.com/renetik/renetik-android/).

Instrumented UI-test toolkit for `androidTest` source sets. It bundles Barista
(Espresso), UI Automator, AndroidX test core, and Renetik Android helpers for
screen interaction, view lookup, idle waiting, and screenshots.

- **`CSEspresso`**: launch helpers, text/view assertions, root actions, random
  clicks/swipes, and view lookup with timeouts.
- **`CSDevice` and `UiDevice` extensions**: wait, press, swipe, click, and
  visibility helpers around UI Automator.
- **`CSScreenshotMaker`**: sequential device screenshots during instrumented
  runs.
- **Framework access**: includes `renetik-android-framework` for app-facing
  UI/controller APIs in tests.

### Installation

Add JitPack and the UI testing dependency:

```gradle
repositories {
    google()
    mavenCentral()
    maven { url = 'https://jitpack.io' }
}

dependencies {
    androidTestImplementation 'com.github.renetik.renetik-android:renetik-android-testing-ui:2.0.1'
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
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import renetik.android.testing.ui.automator.CSDevice.device
import renetik.android.testing.ui.automator.waitForMoreIdle
import renetik.android.testing.ui.espresso.CSEspresso

@RunWith(AndroidJUnit4::class)
class ChecklistUiTest {
    @Test
    fun checklistLaunches() {
        CSEspresso.launch<MainActivity>().use {
            CSEspresso.checkTextDisplayed("Renetik Android")
            device.waitForMoreIdle()
        }
    }
}
```

### Core concepts

- **`renetik.android.testing.ui.espresso`**: Espresso/Barista helpers for
  launching activities, waiting for text, and finding views.
- **`renetik.android.testing.ui.automator`**: `CSDevice`, screen side helpers,
  `UiDevice` extensions, and screenshots.
- **`CSScreenshotMaker`**: repeatable screenshot naming and capture flow.

### Dependencies

- **Renetik modules**: [Framework](../renetik-android-framework),
  [Testing](../renetik-android-testing).
- **External androidTest APIs**: AndroidX Test Core KTX `1.7.0`, UI Automator
  `2.3.0`, Barista `4.3.0`.

### Related Renetik libraries

- [Renetik Android](https://github.com/renetik/renetik-android/)
- [Renetik Android Testing](../renetik-android-testing)
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
