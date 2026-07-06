# Module renetik-android-preset

Presets: named snapshots of store-backed properties with save, load and reload.
Build on top of the store module to give an app user-selectable configurations.

# Package renetik.android.preset

`CSPreset` and its `property` factories, plus `Preset.init()` and the
save/reload lifecycle (`saveAsCurrent`, `reloadInternal`).

# Package renetik.android.preset.property

Preset-aware property types (`value` and `nullable` variants) that persist into
the selected preset item.

# Package renetik.android.preset.context

Store-context plumbing (`CSHasPresetStoreContext`, `PresetStoreContext`) that
routes properties to the active preset store.
