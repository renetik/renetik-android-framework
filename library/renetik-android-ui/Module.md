# Module renetik-android-ui

View classes and Kotlin extensions for building Android UI with less
boilerplate — the foundation for the controller and material modules.

# Package renetik.android.ui.view

CS view classes (`CSLinearLayout`, `CSFrameLayout`, `CSConstraintLayout`,
`CSEditText`, …) and `View`/`ViewGroup` extensions for visibility, dimensions,
margins, positions, animation and touch.

# Package renetik.android.ui.widget

`TextView`, `EditText`, `ImageView`, picker and scroll-view extensions, plus
`LinearLayout` factories (`VerticalLayout`, `HorizontalLayout`).

# Package renetik.android.ui.graphics

The `CSImaging` abstraction with drawable/bitmap helpers (`CSAndroidImaging`,
`CSTileDrawable`); the imaging module supplies a Glide-backed implementation.

# Package renetik.android.ui.protocol

`CSViewInterface` and `CSVisibility` protocols and their extensions, used by
the controller layer to treat CS views uniformly.
