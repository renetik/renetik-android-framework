package cs.android.extensions

fun String.trimNewLines(): String {
    return replace("\n","")
}
