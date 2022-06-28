<<<<<<< HEAD
[![Android CI](https://github.com/renetik/renetik-android/actions/workflows/android.yml/badge.svg)](https://github.com/renetik/renetik-android/actions/workflows/android.yml)

=======
[![Android CI](https://github.com/renetik/renetik-android/workflows/Android%20CI/badge.svg)](https://github.com/renetik/renetik-android/actions/workflows/android.yml)
>>>>>>> 02af2870d01d542f4d1c61db5623faa428b17ad5
# Renetik Android

Framework to enjoy, improve and speed up your application development while writing readable code.
Used as library for music production and performance app Renetik Instruments www.renetik.com as well
as other projects.

```gradle
allprojects {
    repositories {
        // For master-SNAPSHOT
        maven { url 'https://github.com/renetik/maven-snapshot/raw/master/repository' }
        // For release builds
        maven { url 'https://github.com/renetik/maven/raw/master/repository' }
    }
}
```

Step 2. Add the dependency

```gradle
dependencies {
    implementation "com.renetik.library:renetik-android-framework:$renetik_android_version"
}
```

## [Html Documentation](https://renetik.github.io/renetik-android/)
