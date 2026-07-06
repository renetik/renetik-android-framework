[![Android Build](https://github.com/renetik/renetik-android/actions/workflows/android.yml/badge.svg)](https://github.com/renetik/renetik-android/actions/workflows/android.yml)

## Renetik Android Controller

Part of [Renetik Android](https://github.com/renetik/renetik-android/).

View-controller layer for Android apps: lifecycle-aware views, navigation,
dialogs, paging, and data-driven view binding on top of a single Activity.

- **`CSView<V>`**: lifecycle-aware wrapper around an Android `View` with
  parent/child destruction and registrations.
- **`CSActivityView<V>`**: screen-level view bound to Activity resume, pause,
  back, menu, permission, and result hooks.
- **`CSNavigationView` / `CSNavigationItemView`**: push/pop navigation with
  animations and dialog-style presentations.
- **`CSGridView` and `CSPagerView`**: controller helpers for lists, grids, and
  pages.
- **View factories**: `viewFactory` and `addView` helpers for binding lists to
  views.

### Installation

Add JitPack and the controller dependency:

```gradle
repositories {
    google()
    mavenCentral()
    maven { url = 'https://jitpack.io' }
}

dependencies {
    implementation 'com.github.renetik.renetik-android:renetik-android-controller:2.0.1'
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

    fun openDetails() {
        DetailsItemView(this).fullScreen().show()
    }
}
```

The [sample app](../sample) main screen is a `CSActivityView` hosted in a
`CSNavigationView`.

### Core concepts

- **`renetik.android.controller.base`**: `CSView`, `CSActivityView`,
  `CSViewActivity`, lifecycle hooks, dialogs, permissions, and Activity results.
- **`renetik.android.controller.navigation`**: navigation stack, item views,
  animations, and dialog presentations.
- **`renetik.android.controller.pager`**: page adapters and controller-backed
  pager views.
- **`renetik.android.controller.view`**: recycler/grid helpers, wrap-height
  recycler view, and list-to-view factories.

### Dependencies

- **Renetik modules**: [Core](../renetik-android-core),
  [Event](../renetik-android-event), [UI](../renetik-android-ui),
  [Store](../renetik-android-store).
- **External runtime**: AndroidX RecyclerView `1.4.0`, AndroidX Activity
  `1.13.0`.
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
