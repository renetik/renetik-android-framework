# Module renetik-android-store

Key-value stores with typed, observable property delegates. Back a store with
JSON, a file, or Android `SharedPreferences` and read/write it through
`CSProperty`-style delegates that survive reloads.

# Package renetik.android.store

`CSStore` and the `property` / `dataProperty` / `late*` / `null*` delegate
factories for every supported value type.

# Package renetik.android.store.type

Store implementations: `CSJsonObjectStore`, `CSPreferencesStore`,
`CSFileJsonStore`, `CSBundleJsonStore` and friends.

# Package renetik.android.store.property

The `CSStoreProperty` delegate implementations behind the factories.
