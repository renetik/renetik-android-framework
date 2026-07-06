<!---Header--->
[![Android Build](https://github.com/renetik/renetik-android/actions/workflows/android.yml/badge.svg)](https://github.com/renetik/renetik-android/actions/workflows/android.yml)

# Renetik Android Core

Part of [Renetik Android](https://github.com/renetik/renetik-android/).

Core library for the Renetik Android libraries collection.
It provides shared application runtime helpers, Kotlin/Java/Android extensions,
state primitives, logging, math helpers, resources, and optional LeakCanary wiring.

The project favors compact extension files such as `Context+.kt`, `Int+.kt`,
and `CSValue+.kt`. The `+` suffix is intentional and marks files that extend
an existing platform or Renetik type.

Used as a library in many projects and improved while developing new projects.
I am open for [Hire](https://renetik.github.io) or investment in my mobile app music production & performance project Renetik Instruments www.renetik.com.

## Installation

Add JitPack to dependency repositories in `settings.gradle`:

```gradle
dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
        maven { url 'https://jitpack.io' }
    }
}
```

Add the core module to the application or library module:

```gradle
dependencies {
    implementation 'com.github.renetik.renetik-android:renetik-android-core:2.0'
}
```

Use `master-SNAPSHOT` instead of `2.0` to test the latest Renetik Android `master`.

LeakCanary integration is published separately:

```gradle
dependencies {
    debugImplementation 'com.github.renetik.renetik-android:renetik-android-core-leakcanary:2.0'
}
```

## Project Layout

| Path | Purpose |
| --- | --- |
| `renetik-android-core/` | Main `renetik-android-core` Android library artifact. |
| `renetik-android-core-leakcanary/` | Optional `renetik-android-core-leakcanary` integration artifact. |
| `docs/package-map.md` | Package guide for finding source areas quickly. |
| `../library.gradle` | Shared Android library build configuration. |

## Package Map

| Package | Contains |
| --- | --- |
| `renetik.android.core.base` | `CSApplication` and app lifecycle base classes. |
| `renetik.android.core.lang` | Core Renetik contracts, runtime helpers, guards, handlers, and type aliases. |
| `renetik.android.core.lang.value` | Read-only value primitives. |
| `renetik.android.core.lang.variable` | Mutable variable primitives and weak/safe variants. |
| `renetik.android.core.lang.lazy` | Lazy property primitives. |
| `renetik.android.core.lang.result` | Coroutine and result helpers. |
| `renetik.android.core.kotlin` | Kotlin type extensions. |
| `renetik.android.core.java` | Java/JDK type extensions. |
| `renetik.android.core.android` | Android framework extensions and platform helpers by Android package area. |
| `renetik.android.core.android.os` | Android handler, looper, and main-thread helpers. |
| `renetik.android.core.android.graphics` | Android graphics helpers and color types. |
| `renetik.android.core.logging` | Logger abstraction and implementations. |
| `renetik.android.core.math` | Math and point helpers. |

See [docs/package-map.md](docs/package-map.md) for the fuller navigation guide.

## Examples
```kotlin
class CSLazyVarTest {
    @Test
    fun testLazyVar() {
        var testVar: String by lazyVar { "initial" }
        assertEquals("initial", testVar)
        testVar = "test"
        assertEquals("test", testVar)
    }

    @Test
    fun testNullableLazyVar() {
        var testVar: String? by lazyVar { "initial" }
        assertEquals("initial", testVar)
        testVar = "test"
        assertEquals("test", testVar)
        testVar = null
        assertEquals(null, testVar)
    }
}
```
```kotlin
class AnyCSEqualsTest {
    @Test
    fun equalsAny() {
        assertTrue("third".equalsAny("first", "second", "third"))
        assertFalse("fourth".equalsAny("first", "second", "third"))

        val values = listOf("first", "second", "third")
        assertTrue("third" equalsAny values)
        assertFalse("fourth" equalsAny values)
    }

    @Test
    fun equalsNone() {
        assertTrue("fourth".equalsNone("first", "second", "third"))
        assertFalse("second".equalsNone("first", "second", "third"))

        val values = listOf("first", "second", "third")
        assertTrue("fourth" equalsNone values)
        assertFalse("first" equalsNone values)
    }

    @Test
    fun equalsAll() {
        assertTrue("fourth".equalsAll("fourth", "fourth", "fourth"))
        assertFalse("fourth".equalsAll("first", "second", "third"))

        assertTrue("fourth" equalsAll listOf("fourth", "fourth", "fourth"))
        assertFalse("fourth" equalsAll listOf("first", "second", "third"))
    }
}
```
```kotlin
class CSAndroidLoggerTest {

    var event: CSLoggerEvent? = null
    var message: String? = null
    private val listener = { event: CSLoggerEvent, message: String ->
        this.event = event
        this.message = message
    }

    @Test
    fun logWithListener() {
        init(CSAndroidLogger(name = "TestLog", isDebug = true, listener))
        logWarn("test")

        assertEquals(Warn, event)
        val messageEnd =
            "renetik.android.core.logging.CSAndroidLoggerTest\$logWithListener(CSAndroidLoggerTest.kt:26) test"
        assertTrue(message!!.endsWith(messageEnd))
    }

    @Test
    fun isDebug() {
        init(CSAndroidLogger(name = "TestLog", isDebug = false, listener))
        logDebug { "test" }
        assertNull(event)
        assertNull(message)

        init(CSAndroidLogger(name = "TestLog", isDebug = true, listener))
        logDebug { "test2" }
        assertEquals(Debug, event)
        val messageEnd =
            "renetik.android.core.logging.CSAndroidLoggerTest\$isDebug(CSAndroidLoggerTest.kt:42) test2"
        assertTrue(message!!.endsWith(messageEnd))
    }
}
```

## Renetik Android Libraries

See [Renetik Android](https://github.com/renetik/renetik-android/) for all modules and release coordinates.
