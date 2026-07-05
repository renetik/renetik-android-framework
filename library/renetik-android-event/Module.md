# Module renetik-android-event

Reactive core of the framework: type-safe events, observable properties,
disposable registrations, and coroutine integration.

# Package renetik.android.event

`CSEvent<T>` — type-safe signals with `listen`, `listenOnce`, `pause`/`resume`
and optional main-thread delivery.

# Package renetik.android.event.property

`CSProperty<T>` — observable values with `onChange`, `computed`, two-way
`connect`, pausable updates and Kotlin delegate helpers.

# Package renetik.android.event.registration

`CSRegistration` — disposable subscriptions with lifecycle helpers,
`pause`/`resume` and clean teardown.

# Package renetik.android.event.lifecycle

`CSModel` / `CSHasDestruct` — parent-child object lifecycles with
`eventDestruct` and `destruct()`.
