# Module renetik-android-core-leakcanary

LeakCanary wiring for the framework's `CSLeakCanary` facade in
`renetik-android-core`. Add it as a `debugImplementation` only; the facade
stays a no-op in release builds.

# Package renetik.android.core.leakcanary

`CSLeakCanaryProvider` installs the implementation automatically;
`CSLeakCanaryImplementation` enables/disables heap dumping, labels leaks with
Renetik object fields, and is a safe no-op under test runners.
