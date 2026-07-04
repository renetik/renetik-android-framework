[![Build & Tests](https://github.com/renetik/renetik-android-framework/workflows/Android%20Build/badge.svg)](https://github.com/renetik/renetik-android-framework/actions/workflows/android.yml)

# Renetik Android Framework

Android framework and related Renetik Android modules for application development.

Repository: [https://github.com/renetik/renetik-android-framework](https://github.com/renetik/renetik-android-framework/)

## Usage

```gradle
repositories {
    google()
    mavenCentral()
    maven { url = 'https://jitpack.io' }
}
```

Use `master-SNAPSHOT` while testing current `master`, or `2.0` for the reset release.

```gradle
dependencies {
    implementation 'com.github.renetik.renetik-android-framework:renetik-android-framework:2.0'
}
```

## Modules

| Module | Snapshot dependency | Release dependency |
| --- | --- | --- |
| `renetik-android-testing` | `com.github.renetik.renetik-android-framework:renetik-android-testing:master-SNAPSHOT` | `com.github.renetik.renetik-android-framework:renetik-android-testing:2.0` |
| `renetik-android-core` | `com.github.renetik.renetik-android-framework:renetik-android-core:master-SNAPSHOT` | `com.github.renetik.renetik-android-framework:renetik-android-core:2.0` |
| `renetik-android-core-leakcanary` | `com.github.renetik.renetik-android-framework:renetik-android-core-leakcanary:master-SNAPSHOT` | `com.github.renetik.renetik-android-framework:renetik-android-core-leakcanary:2.0` |
| `renetik-android-event` | `com.github.renetik.renetik-android-framework:renetik-android-event:master-SNAPSHOT` | `com.github.renetik.renetik-android-framework:renetik-android-event:2.0` |
| `renetik-android-json` | `com.github.renetik.renetik-android-framework:renetik-android-json:master-SNAPSHOT` | `com.github.renetik.renetik-android-framework:renetik-android-json:2.0` |
| `renetik-android-store` | `com.github.renetik.renetik-android-framework:renetik-android-store:master-SNAPSHOT` | `com.github.renetik.renetik-android-framework:renetik-android-store:2.0` |
| `renetik-android-preset` | `com.github.renetik.renetik-android-framework:renetik-android-preset:master-SNAPSHOT` | `com.github.renetik.renetik-android-framework:renetik-android-preset:2.0` |
| `renetik-android-ui` | `com.github.renetik.renetik-android-framework:renetik-android-ui:master-SNAPSHOT` | `com.github.renetik.renetik-android-framework:renetik-android-ui:2.0` |
| `renetik-android-ui-picker` | `com.github.renetik.renetik-android-framework:renetik-android-ui-picker:master-SNAPSHOT` | `com.github.renetik.renetik-android-framework:renetik-android-ui-picker:2.0` |
| `renetik-android-material` | `com.github.renetik.renetik-android-framework:renetik-android-material:master-SNAPSHOT` | `com.github.renetik.renetik-android-framework:renetik-android-material:2.0` |
| `renetik-android-imaging` | `com.github.renetik.renetik-android-framework:renetik-android-imaging:master-SNAPSHOT` | `com.github.renetik.renetik-android-framework:renetik-android-imaging:2.0` |
| `renetik-android-controller` | `com.github.renetik.renetik-android-framework:renetik-android-controller:master-SNAPSHOT` | `com.github.renetik.renetik-android-framework:renetik-android-controller:2.0` |
| `renetik-android-framework` | `com.github.renetik.renetik-android-framework:renetik-android-framework:master-SNAPSHOT` | `com.github.renetik.renetik-android-framework:renetik-android-framework:2.0` |
| `renetik-android-testing-ui` | `com.github.renetik.renetik-android-framework:renetik-android-testing-ui:master-SNAPSHOT` | `com.github.renetik.renetik-android-framework:renetik-android-testing-ui:2.0` |

Module README files live next to each module under `library/`.

## Verification

Run the framework build and consumer smoke app before publishing a release:

```sh
./gradlew build --no-daemon --no-configuration-cache
./gradlew publishToMavenLocal --no-daemon --no-configuration-cache -Pgroup=com.github.renetik.renetik-android-framework -Pversion=0.0.0-smoke
./gradlew -p consumer-smoke assembleDebug --no-daemon --no-configuration-cache -PsmokeVersion=0.0.0-smoke
```

After pushing `master` or creating a release tag, verify JitPack POMs:

```sh
./verify_jitpack.sh master-SNAPSHOT
./verify_jitpack.sh 2.0
```
