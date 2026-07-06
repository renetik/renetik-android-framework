[![Android Build](https://github.com/renetik/renetik-android/actions/workflows/android.yml/badge.svg)](https://github.com/renetik/renetik-android/actions/workflows/android.yml)

## Renetik Android Event

Part of [Renetik Android](https://github.com/renetik/renetik-android/).

Reactive core of the framework: type-safe events, observable properties,
disposable registrations, lifecycle primitives, and coroutine integration.

- **Events**: type-safe signals with `listen`, `listenOnce`, `pause`, `resume`,
  `cancel`, and optional main-thread delivery.
- **Properties**: observable values with `onChange`, `computed`, two-way
  `connect`, pausable updates, and Kotlin delegate helpers.
- **Registrations**: disposable subscriptions with lifecycle helpers, pause,
  resume, and clean teardown.
- **Lifecycle**: `CSModel` and `CSHasDestruct` for parent-child object
  lifetimes.
- **Coroutines**: suspend events/properties, wait helpers, and launch helpers.

### Installation

Add JitPack and the event dependency:

```gradle
repositories {
    google()
    mavenCentral()
    maven { url = 'https://jitpack.io' }
}

dependencies {
    implementation 'com.github.renetik.renetik-android:renetik-android-event:2.0.1'
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

Events:

```kotlin
import renetik.android.event.CSEvent.Companion.event

val onTick = event<Unit>()
val registration = onTick.listen { println("tick") }

onTick.fire()
registration.cancel()
```

Properties:

```kotlin
import renetik.android.event.property.CSProperty.Companion.property

var name by property("Guest") { println("changed to $it") }
name = "Alice"
```

Listen once and pause/resume:

```kotlin
val clicks = event<Unit>()
clicks.listenOnce { println("first and only") }

val registration = clicks.listen { println("every time") }
registration.pause()
clicks.fire()
registration.resume()
clicks.fire()
```

Two-way connect and computed properties:

```kotlin
val a = property(0)
val b = property(0)

a.connect(b)

val percent = a.computed(
    from = { it / 100f },
    to = { (it * 100).toInt() }
)
```

### Core concepts

- **`CSEvent<T>`**: `listen`, `listenOnce`, `fire(T)`, `pause`, `resume`,
  `isListened`, `clear`, and `onMain`.
- **`CSProperty<T>`**: `value`, `onChange`, `fireChange`, `pause`, `resume`,
  `computed`, `connect`, `paused`, and delegate support.
- **`CSRegistration`**: result of `listen` and `onChange`; supports `pause`,
  `resume`, `cancel`, `isActive`, and `eventCancel`.
- **`CSModel` / `CSHasDestruct`**: parent-child lifecycles with
  `eventDestruct` and `destruct()`.

### Delegates

Delegates derive values and signals from existing events/properties with little
boilerplate. They return lightweight `CSHasChangeValue<T>` or `CSProperty<T>`
instances that stay in sync.

- From `CSEvent<T>`: `delegate { from() }` and `delegateLate { from() }`.
- From `CSProperty<T>` and `CSHasChangeValue<T>`: `computed(from, to)`,
  `computed { get, set }`, `delegateValue { transform(it) }`, and
  `delegate { combine(...) }`.
- Convenience delegates cover boolean logic, equality checks, null checks, type
  casts, list delegates, and related transforms.

```kotlin
val meterEvent = event<Int>()
val latest = meterEvent.delegate { it }

val maybeLatest = meterEvent.delegateLate { it }

val volume = property(50)
val volumePercent = volume.computed(
    from = { it / 100f },
    to = { (it * 100).toInt() }
)

val isMuted = property(false)
val isAudible = !isMuted
```

### Threading

Deliver event callbacks on the main thread by chaining `onMain(owner)` on the
created event:

```kotlin
val owner = /* something implementing CSHasDestruct */
val event = CSEvent.event<Unit>().onMain(owner)

event.listen { /* runs on main */ }
```

### Coroutines

Suspend-friendly primitives and helpers:

- Await values with `waitFor { condition(it) }`, `waitForTrue()`,
  `waitForFalse()`, `suspendIfTrue()`, and `suspendIfFalse()`.
- Use `CSSuspendEvent<T>` and `CSSuspendProperty<T>` for suspending listeners.
- Launch work from changes with `onChangeLaunch`, `actionLaunch`,
  `onTrueLaunch`, and `onFalseLaunch`.
- Use registration-owned launch helpers from `CSHasRegistrations`.

```kotlin
val isReady = property(false)
scope.launch { isReady.waitForTrue() }

val value = property(0)
scope.launch { value.waitFor { it > 10 } }

val onReady = CSSuspendEvent.suspendEvent<Unit>()
scope.launch { onReady.listen { /* suspending work */ } }
scope.launch { onReady() }

value.onChangeLaunch { newValue ->
    // suspending work using newValue
}
```

### Dependencies

- **Renetik modules**: [Core](../renetik-android-core).
- **External runtime**: none beyond the shared Android/Kotlin stack.
- **Test scope**: [Testing](../renetik-android-testing).

### Related Renetik libraries

- [Renetik Android](https://github.com/renetik/renetik-android/)
- [Renetik Android Core](../renetik-android-core)
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
