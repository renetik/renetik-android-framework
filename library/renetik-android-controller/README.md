[![Android Build](https://github.com/renetik/renetik-android-framework/actions/workflows/android.yml/badge.svg)](https://github.com/renetik/renetik-android-framework/actions/workflows/android.yml)

## Renetik Android Controller

Part of [Renetik Android Framework](https://github.com/renetik/renetik-android-framework/).

View-controller layer: lifecycle-aware views, navigation and paging on top of a single Activity.

- **`CSView<V>`**: a lifecycle-aware wrapper around an Android `View` with parent/child destruction, registrations and lazy view creation.
- **`CSActivityView<V>`**: `CSView` bound to activity lifecycle (resume/pause/back/menu), the building block for screens.
- **`CSNavigationView` / `CSNavigationItemView`**: push/pop navigation with animations and dialogs inside one Activity.
- **`CSGridView`, `CSPagerView`**: data-driven grid and pager controllers.
- **`renetik.android.controller.view`**: `viewFactory`/`addView` list-to-views binding and helper views like `CSWrapHeightRecyclerView`.

### Installation

```gradle
repositories {
    google()
    mavenCentral()
    maven { url = 'https://jitpack.io' }
}

dependencies {
    implementation 'com.github.renetik.renetik-android-framework:renetik-android-controller:2.0'
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
class MainActivity : CSViewActivity<MainView>() {
    override fun createView() = MainView(this)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView()
    }
}

class MainView(activity: MainActivity) :
    CSActivityView<FrameLayout>(activity, layout(cs_frame_match)) {
    override var navigation: CSNavigationView? = CSNavigationView(this)
}

// push and pop screens
SomeItemView(mainView).fullScreen().push()   // CSNavigationItemView
navigation.pop()
```

The [sample app](../../sample) main screen is a `CSActivityView` inside `CSNavigationView`.

### License
This library is released under the terms of the license in `LICENSE.txt`.
