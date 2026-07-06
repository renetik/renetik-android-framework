[![Android Build](https://github.com/renetik/renetik-android/actions/workflows/android.yml/badge.svg)](https://github.com/renetik/renetik-android/actions/workflows/android.yml)

## Renetik Android Testing

Part of [Renetik Android](https://github.com/renetik/renetik-android/).

Shared unit-test stack for Renetik Android libraries and consumers: JUnit4,
Robolectric, MockK, kotlinx-coroutines-test, and small helpers for assertions
and Robolectric context access.

- **Bundled test APIs**: exposes JUnit4, Robolectric, MockK, and coroutines-test
  as `api` dependencies.
- **`TestApplication`**: Robolectric application with an AppCompat theme.
- **`context` helper**: direct access to the Robolectric application context.
- **`CSAssert`**: compact assertion helpers used across Renetik Android tests.

### Installation

Add JitPack and the testing dependency:

```gradle
repositories {
    google()
    mavenCentral()
    maven { url = 'https://jitpack.io' }
}

dependencies {
    testImplementation 'com.github.renetik.renetik-android:renetik-android-testing:2.0.1'
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
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import renetik.android.testing.CSAssert.assert
import renetik.android.testing.TestApplication
import renetik.android.testing.context

@RunWith(RobolectricTestRunner::class)
@Config(application = TestApplication::class)
class StoreTest {
    @Test
    fun hasContext() {
        assert(context.packageName.isNotEmpty())
    }
}
```

### Core concepts

- **`CSAssert`**: assertion helpers for equality, booleans, thrown exceptions,
  and substring checks.
- **`TestApplication`**: AppCompat-themed application for Robolectric tests.
- **`context`**: shortcut to `RuntimeEnvironment.getApplication()`.
- **Bundled dependencies**: use one Renetik coordinate instead of repeating the
  common test stack in every module.

### Dependencies

- **External test APIs**: JUnit `4.13.2`, MockK `1.14.9`, Robolectric `4.16.1`,
  kotlinx-coroutines-test `1.11.0`.
- **Renetik modules**: none.
- **Shared stack**: Kotlin stdlib/reflect, AndroidX Core KTX, and AppCompat from
  the common library build.

### Related Renetik libraries

- [Renetik Android](https://github.com/renetik/renetik-android/)
- [Renetik Android Testing UI](../renetik-android-testing-ui)

### Contributing

Issues and PRs are welcome. Please include a clear description and small,
focused changes.

### License

This library is released under the terms of [LICENSE.txt](../LICENSE.txt).

---

If you find this useful, consider supporting the broader Renetik Instruments
effort at [renetik.com](https://www.renetik.com) or get in touch for
[Hire](https://renetik.github.io).
