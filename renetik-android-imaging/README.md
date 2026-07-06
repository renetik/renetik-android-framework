[![Android Build](https://github.com/renetik/renetik-android/actions/workflows/android.yml/badge.svg)](https://github.com/renetik/renetik-android/actions/workflows/android.yml)

## Renetik Android Imaging

Part of [Renetik Android](https://github.com/renetik/renetik-android/).

Image loading and processing built on [Glide](https://github.com/bumptech/glide).
It supplies the Glide-backed implementation of the UI module's `CSImaging`
abstraction and bitmap/file/URI helpers used by Renetik Android apps.

- **`CSGlideImaging`**: loads UI `ImageView.image(...)` requests through Glide.
- **Bitmap and file helpers**: `write`, `loadBitmap`, `resizeImage`, `scale`,
  and EXIF orientation correction.
- **Glide helpers**: progress callbacks, request adapters, and
  `BorderBitmapTransformation`.
- **UI integration**: works behind the `CSImaging` abstraction from
  `renetik-android-ui`.

### Installation

Add JitPack and the imaging dependency:

```gradle
repositories {
    google()
    mavenCentral()
    maven { url = 'https://jitpack.io' }
}

dependencies {
    implementation 'com.github.renetik.renetik-android:renetik-android-imaging:2.0.1'
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
import renetik.android.imaging.CSGlideImaging
import renetik.android.imaging.scale
import renetik.android.imaging.write
import renetik.android.ui.widget.image

CSGlideImaging.initialize()               // once, for example in Application

file.write(bitmap)                        // save bitmap as JPEG
val thumb = file.loadBitmap()?.scale(128) // decode and aspect-fit scale
imageView.image(file)                     // load through Glide
```

### Core concepts

- **`CSGlideImaging`**: Glide-backed `CSImaging` implementation.
- **`File`/`Uri`/`Bitmap` extensions**: disk, decoding, scaling, resizing, and
  orientation helpers.
- **`ImageView+Glide`**: property-aware Glide loading helpers for image views.

### Dependencies

- **Renetik modules**: [Core](../renetik-android-core),
  [Event](../renetik-android-event), [UI](../renetik-android-ui).
- **External runtime**: Glide `5.0.7`, AndroidX ExifInterface `1.4.2`.
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
