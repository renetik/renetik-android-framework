package renetik.android.store.property

interface CSLateStoreProperty<T> : CSStoreProperty<T> {
    val isLoaded: Boolean
}