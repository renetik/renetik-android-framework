[![Android Build](https://github.com/renetik/renetik-android-framework/actions/workflows/android.yml/badge.svg)](https://github.com/renetik/renetik-android-framework/actions/workflows/android.yml)

## Renetik Android Testing UI

Part of [Renetik Android Framework](https://github.com/renetik/renetik-android-framework/).

Instrumented UI testing toolkit used in `androidTest` source sets. Bundles
[Barista](https://github.com/AdevintaSpain/Barista) (Espresso),
UI Automator and AndroidX test core, plus framework helpers.

- **`CSAutomator`**: UI Automator helpers (`UIDevice` extensions, screen sides, scrolling).
- **`CSScreenshotMaker`**: device screenshots during instrumented runs.
- **`CSEspresso`**: Espresso/Barista glue for CS views.

### Installation

```gradle
repositories {
    google()
    mavenCentral()
    maven { url = 'https://jitpack.io' }
}

dependencies {
    androidTestImplementation 'com.github.renetik.renetik-android-framework:renetik-android-testing-ui:2.0'
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
@RunWith(AndroidJUnit4::class)
class ChecklistUiTest {
    @Test
    fun checklistLaunches() {
        ActivityScenario.launch(MainActivity::class.java).use { scenario ->
            scenario.onActivity { activity -> /* assert on CS views */ }
        }
    }
}
```

See the [sample app](../sample) `androidTest` for a working example.

### License
This library is released under the terms of the license in `LICENSE.txt`.
