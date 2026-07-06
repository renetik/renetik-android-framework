# Module renetik-android-core

Kotlin/Android foundation for the framework: logging, language and collection
helpers, math and geometry, colors, file and content helpers, and lazy /
atomic / variable primitives. Layered to mirror the platform:
`core.android.*`, `core.java.*`, `core.kotlin.*`, `core.lang.*`.

Every other module depends on core.

# Package renetik.android.core.lang

Core language primitives: `CSLazyVal`/`CSLazyVar`, `CSVariable`, atomics,
results, tuples, and the `CSLeakCanary` facade wired by the
`renetik-android-core-leakcanary` module.

# Package renetik.android.core.android.graphics

`CSColor` and color helpers for converting, blending and applying alpha.

# Package renetik.android.core.android.content

Context, resources and file helpers such as `dpToPixel`, `temporaryFolder`
and shared-preferences access.
