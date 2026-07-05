[![Build & Tests](https://github.com/renetik/renetik-android-framework/workflows/Android%20Build/badge.svg)](https://github.com/renetik/renetik-android-framework/actions/workflows/android.yml)
[![API Docs](https://github.com/renetik/renetik-android-framework/actions/workflows/docs.yml/badge.svg)](https://renetik.github.io/renetik-android-framework/)
[![JitPack](https://jitpack.io/v/renetik/renetik-android-framework.svg)](https://jitpack.io/#renetik/renetik-android-framework)

# Renetik Android Framework

A pragmatic Kotlin framework for Android application development: reactive
events and properties, JSON, persistent stores and presets, a view-controller
layer over a single Activity, and a set of UI widgets and extensions — the
foundation of the [Renetik Instruments](https://www.renetik.com) apps.

- **API docs**: https://renetik.github.io/renetik-android-framework/
- **Sample**: [`sample/`](sample) — its main screen is a live checklist that
  exercises every published module at runtime.
- **Changelog**: [CHANGELOG.md](CHANGELOG.md)

## Quickstart

```gradle
repositories {
    google()
    mavenCentral()
    maven { url = 'https://jitpack.io' }
}

dependencies {
    // everything at once (aggregate artifact):
    implementation 'com.github.renetik.renetik-android-framework:renetik-android-framework:2.0'
    debugImplementation 'com.github.renetik.renetik-android-framework:renetik-android-core-leakcanary:2.0'
}
```

Use `master-SNAPSHOT` instead of `2.0` to follow the current `master`.
Prefer individual modules from the matrix below for a smaller footprint.

## Modules

| Module | Description |
| --- | --- |
| [`renetik-android-core`](library/renetik-android-core) | Kotlin/Android foundation: logging, lang, math, colors, files, lazy/atomic/variable primitives |
| [`renetik-android-core-leakcanary`](library/renetik-android-core-leakcanary) | LeakCanary wiring for core's `CSLeakCanary` facade (debug builds) |
| [`renetik-android-event`](library/renetik-android-event) | Events, observable properties, registrations, coroutines integration |
| [`renetik-android-json`](library/renetik-android-json) | JSON mapping for objects, lists and maps with `CSJsonObject` |
| [`renetik-android-store`](library/renetik-android-store) | Key-value stores (json/file/preferences) with typed property delegates |
| [`renetik-android-preset`](library/renetik-android-preset) | Presets: named snapshots of store-backed properties with save/load |
| [`renetik-android-ui`](library/renetik-android-ui) | CS views, view/widget extensions, graphics helpers, view protocols |
| [`renetik-android-ui-picker`](library/renetik-android-ui-picker) | `CSNumberPicker` wheel bound to framework properties |
| [`renetik-android-material`](library/renetik-android-material) | Material Components: sliders, forms, dialogs, snackbar extensions |
| [`renetik-android-imaging`](library/renetik-android-imaging) | Glide-based image loading and bitmap/file/uri processing |
| [`renetik-android-controller`](library/renetik-android-controller) | View-controller layer: `CSActivityView`, navigation, grid, pager |
| [`renetik-android-framework`](library/renetik-android-framework) | Aggregate artifact depending on all runtime modules above |
| [`renetik-android-testing`](library/renetik-android-testing) | Unit-test stack: JUnit4 + Robolectric + MockK + coroutines-test |
| [`renetik-android-testing-ui`](library/renetik-android-testing-ui) | Instrumented-test stack: Barista/Espresso + UI Automator helpers |

All artifacts share the group `com.github.renetik.renetik-android-framework`
and are published via [JitPack](https://jitpack.io/#renetik/renetik-android-framework).

## Conventions

- **`CS` class prefix** — framework types are prefixed `CS` (`CSView`,
  `CSProperty`, `CSJsonObject`) so they never collide with platform names.
- **`Type+Topic.kt` extension files** — extensions of `Type` live in a file
  named after the receiver plus a topic, in the package of the type's domain:
  - lowercase topic = function name: `CSHasChangeValue+onChange.kt`
  - PascalCase topic = type or functionality name:
    `CSHasChangeValue+Boolean.kt`, `View+Appearance.kt`, `+Coroutines.kt`
  - plain `Type+.kt` holds the type's general extensions.
- **One topic per file** — small focused files instead of grab-bag modules.

## Sample app

[`sample/`](sample) is a standalone Gradle project consuming the published
artifacts (from mavenLocal in CI, JitPack otherwise) — proving all libraries
resolve. Its main screen is a module checklist built with the framework's own
controller/ui DSL: every row runs a real mini-exercise of one module and
shows ✓/✗, with embedded picker/slider/image demos and a navigation
push/pop demo.

## Verification

Run the framework build and sample app before publishing a release:

```sh
./gradlew build --no-daemon --no-configuration-cache
./gradlew publishToMavenLocal --no-daemon --no-configuration-cache -Pgroup=com.github.renetik.renetik-android-framework -Pversion=0.0.0-smoke
./gradlew -p sample assembleDebug --no-daemon --no-configuration-cache -PsmokeVersion=0.0.0-smoke
./gradlew -p sample testDebugUnitTest --no-daemon --no-configuration-cache -PsmokeVersion=0.0.0-smoke
```

Or everything at once, including tagging and JitPack verification:

```sh
./release.sh 2.0
```

After pushing `master` or creating a release tag, verify JitPack POMs:

```sh
./verify_jitpack.sh master-SNAPSHOT
./verify_jitpack.sh 2.0
```

API docs are generated with Dokka (`./gradlew :dokkaGenerate`, output in
`build/dokka/html`) and deployed to GitHub Pages by
[docs.yml](.github/workflows/docs.yml).

## License

Released under the terms of [LICENSE.txt](LICENSE.txt).

—
If you find this useful, consider supporting the broader Renetik Instruments
effort at https://www.renetik.com or get in touch for [hire](https://renetik.github.io).
