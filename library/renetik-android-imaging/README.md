[![Android Build](https://github.com/renetik/renetik-android-framework/actions/workflows/android.yml/badge.svg)](https://github.com/renetik/renetik-android-framework/actions/workflows/android.yml)

## Renetik Android Imaging

Part of [Renetik Android Framework](https://github.com/renetik/renetik-android-framework/).

Image loading and processing built on [Glide](https://github.com/bumptech/glide).

- **`CSGlideImaging`**: Glide-backed implementation of the ui module's `CSImaging`, so `ImageView.image(...)` extensions load through Glide.
- **`File`/`Uri`/`Bitmap` extensions**: `write`, `loadBitmap`, `resizeImage`, `scale`, EXIF orientation fix (`createFixOrientationMatrix`).
- **`BorderBitmapTransformation`** and Glide progress/request helpers.

### Installation

```gradle
repositories {
    google()
    mavenCentral()
    maven { url = 'https://jitpack.io' }
}

dependencies {
    implementation 'com.github.renetik.renetik-android-framework:renetik-android-imaging:2.0'
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
import renetik.android.imaging.CSGlideImaging
import renetik.android.imaging.scale
import renetik.android.imaging.write

CSGlideImaging.initialize()                    // once, e.g. in Application

file.write(bitmap)                             // save bitmap as JPEG
val thumb = file.loadBitmap()?.scale(128)      // decode + aspect-fit scale
imageView.image(file)                          // load via Glide
```

### License
This library is released under the terms of the license in `LICENSE.txt`.
