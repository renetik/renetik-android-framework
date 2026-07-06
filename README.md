[![Build & Tests](https://github.com/renetik/renetik-android/workflows/Android%20Build/badge.svg)](https://github.com/renetik/renetik-android/actions/workflows/android.yml)
[![API Docs](https://github.com/renetik/renetik-android/actions/workflows/docs.yml/badge.svg)](https://renetik.github.io/renetik-android/)
[![JitPack](https://jitpack.io/v/renetik/renetik-android.svg)](https://jitpack.io/#renetik/renetik-android)

# Renetik Android

A pragmatic Kotlin library for Android application development: reactive
events and properties, JSON, persistent stores and presets, a view-controller
layer over a single Activity, and a set of UI widgets and extensions — the
foundation of the [Renetik Instruments](https://www.renetik.com) apps.

- **API docs**: https://renetik.github.io/renetik-android/
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
    implementation 'com.github.renetik.renetik-android:renetik-android-framework:2.0.1'
    debugImplementation 'com.github.renetik.renetik-android:renetik-android-core-leakcanary:2.0.1'
}
```

Use `master-SNAPSHOT` instead of `2.0.1` to follow the current `master`.
Prefer individual modules from the matrix below for a smaller footprint.

## Modules

| Module | Description |
| --- | --- |
| [`renetik-android-core`](renetik-android-core) | Kotlin/Android foundation: logging, lang, math, colors, files, lazy/atomic/variable primitives |
| [`renetik-android-core-leakcanary`](renetik-android-core-leakcanary) | LeakCanary wiring for core's `CSLeakCanary` facade (debug builds) |
| [`renetik-android-event`](renetik-android-event) | Events, observable properties, registrations, coroutines integration |
| [`renetik-android-json`](renetik-android-json) | JSON mapping for objects, lists and maps with `CSJsonObject` |
| [`renetik-android-store`](renetik-android-store) | Key-value stores (json/file/preferences) with typed property delegates |
| [`renetik-android-preset`](renetik-android-preset) | Presets: named snapshots of store-backed properties with save/load |
| [`renetik-android-ui`](renetik-android-ui) | CS views, view/widget extensions, graphics helpers, view protocols |
| [`renetik-android-ui-picker`](renetik-android-ui-picker) | `CSNumberPicker` wheel bound to Renetik Android properties |
| [`renetik-android-material`](renetik-android-material) | Material Components: sliders, forms, dialogs, snackbar extensions |
| [`renetik-android-imaging`](renetik-android-imaging) | Glide-based image loading and bitmap/file/uri processing |
| [`renetik-android-controller`](renetik-android-controller) | View-controller layer: `CSActivityView`, navigation, grid, pager |
| [`renetik-android-framework`](renetik-android-framework) | Aggregate artifact depending on all runtime modules above |
| [`renetik-android-testing`](renetik-android-testing) | Unit-test stack: JUnit4 + Robolectric + MockK + coroutines-test |
| [`renetik-android-testing-ui`](renetik-android-testing-ui) | Instrumented-test stack: Barista/Espresso + UI Automator helpers |

All artifacts share the group `com.github.renetik.renetik-android`
and are published via [JitPack](https://jitpack.io/#renetik/renetik-android).

## Conventions

- **`CS` class prefix** — Renetik Android types are prefixed `CS` (`CSView`,
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
resolve. Its main screen is a module checklist built with Renetik Android's own
controller/ui DSL: every row runs a real mini-exercise of one module and
shows ✓/✗, with embedded picker/slider/image demos and a navigation
push/pop demo.

## Verification

Run the Renetik Android build and sample app before publishing a release or
when checking a local change:

```sh
./gradlew build --no-daemon --no-configuration-cache
./gradlew publishToMavenLocal --no-daemon --no-configuration-cache -Pgroup=com.github.renetik.renetik-android -Pversion=0.0.0-smoke
./gradlew -p sample assembleDebug --no-daemon --no-configuration-cache -PsmokeVersion=0.0.0-smoke
./gradlew -p sample testDebugUnitTest --no-daemon --no-configuration-cache -PsmokeVersion=0.0.0-smoke
```

After pushing `master` or creating a release tag, verify JitPack POMs:

```sh
./verify_jitpack.sh master-SNAPSHOT
./verify_jitpack.sh 2.0.1
```

Release publishing is separate from verification. See [RELEASE.md](RELEASE.md)
for the full release process, including how `./release.sh <version>` tags the
repository and verifies JitPack.

### API docs

The docs site is a curated landing page ([docs/index.html](docs/index.html))
that lists the modules in product order (foundation → UI → testing → aggregate)
with descriptions, linking into the per-module Dokka API pages. Each module's
page gets its description and package guidance from a `Module.md` next to it.

Build the full site locally with `./build_docs.sh` (landing page at
`build/site/index.html`, Dokka output under `build/site/api/`). On push to
`master`, [docs.yml](.github/workflows/docs.yml) assembles the same site and
deploys it to GitHub Pages.

## License

Released under the terms of [LICENSE.txt](LICENSE.txt).

—
If you find this useful, consider supporting the broader Renetik Instruments
effort at https://www.renetik.com or get in touch for [hire](https://renetik.github.io).
