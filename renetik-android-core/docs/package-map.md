# Package Map

This library is organized around a small set of source areas. Kotlin package
names remain the main navigation boundary, while files named like `Context+.kt`
or `CSValue+.kt` contain extensions for the type named before `+`.

## Modules

| Module folder | Gradle project | Artifact |
| --- | --- | --- |
| `library/` | `:renetik-android-core` | `renetik-android-core` |
| `leakcanary/` | `:renetik-android-core-leakcanary` | `renetik-android-core-leakcanary` |

## Core Packages

| Package | Use when looking for |
| --- | --- |
| `renetik.android.core.base` | Application base classes, app singleton access, and lifecycle hooks. |
| `renetik.android.core.lang` | Core contracts, runtime helpers, guards, handlers, type aliases, resource wrappers, and small domain interfaces. |
| `renetik.android.core.lang.atomic` | Atomic and producer/consumer state helpers. |
| `renetik.android.core.lang.direction` | Direction enums and direction helpers. |
| `renetik.android.core.lang.lazy` | Lazy value and lazy variable delegates. |
| `renetik.android.core.lang.result` | `CSResult`, coroutine scopes, dispatchers, jobs, and continuation helpers. |
| `renetik.android.core.lang.tuples` | Pair and larger tuple helpers. |
| `renetik.android.core.lang.value` | Read-only value abstractions and value extensions. |
| `renetik.android.core.lang.variable` | Mutable value abstractions, weak variables, safe variables, computed variables, and variable extensions. |
| `renetik.android.core.logging` | Logger interfaces, global logging facade, Android logger, print logger, and dummy logger. |
| `renetik.android.core.math` | Math helpers and `CSPoint`. |
| `renetik.android.core.util` | Locale and memory utilities. |

## Extension Packages

| Package | Extends |
| --- | --- |
| `renetik.android.core.kotlin` | Kotlin core types such as `Any`, `Throwable`, `Result`, arrays, and lazy values. |
| `renetik.android.core.kotlin.collections` | Kotlin collection types. |
| `renetik.android.core.kotlin.primitives` | Primitive and primitive-like types such as `Int`, `Float`, `String`, `Boolean`, and arrays. |
| `renetik.android.core.kotlin.ranges` | Kotlin range types. |
| `renetik.android.core.kotlin.reflect` | Kotlin reflection types. |
| `renetik.android.core.kotlin.sequences` | Kotlin sequence types. |
| `renetik.android.core.kotlin.text` | Kotlin text builder types. |
| `renetik.android.core.java` | General Java/JDK helpers. |
| `renetik.android.core.java.io` | `File`, `InputStream`, `Reader`, and `Closeable`. |
| `renetik.android.core.java.lang` | `Class` and system helpers. |
| `renetik.android.core.java.util` | `Date`, `Calendar`, timers, and related helpers. |
| `renetik.android.core.java.util.concurrent` | Executors, futures, scheduled executors, and thread factories. |
| `renetik.android.core.android.app` | Android `Application`. |
| `renetik.android.core.android.bluetooth` | Android Bluetooth manager and adapter helpers. |
| `renetik.android.core.android.content` | Android `Context`, `Intent`, preferences, services, display, files, resources, permissions, URLs, locale, and toast helpers. |
| `renetik.android.core.android.content.res` | Android resources and assets. |
| `renetik.android.core.android.graphics` | Android graphics framework types, color helpers, and `CSColor`/`CSMaterialColor`. |
| `renetik.android.core.android.media` | Android media framework types. |
| `renetik.android.core.android.net` | Android `Uri` helpers. |
| `renetik.android.core.android.opengl` | OpenGL/EGL helpers such as `CSMaxTextureSize`. |
| `renetik.android.core.android.os` | Android `Handler`, looper, and main-thread helpers. |
| `renetik.android.core.android.util` | Android utility helpers such as display metrics. |

## Optional Packages

| Package | Use when looking for |
| --- | --- |
| `renetik.android.core.leakcanary` | LeakCanary implementation and provider installed by the optional debug artifact. |

## Naming Rules

- `Type+.kt` contains extension properties or functions for `Type`.
- `Type+Topic.kt` contains grouped extensions for `Type` around one topic.
- `CS*` names are Renetik primitives, contracts, or helpers.
- Do not use `CSExtension` or `+CS` as extension-file suffixes.
- Tests mirror the package they cover under `library/src/test/java`.
