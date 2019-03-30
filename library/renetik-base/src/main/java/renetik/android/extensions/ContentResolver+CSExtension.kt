import android.content.ContentResolver
import android.net.Uri
import android.provider.MediaStore

fun ContentResolver.getContentName(uri: Uri): String? {
    query(uri, null, null, null, null).use { cursor ->
        cursor!!.moveToFirst()
        val nameIndex = cursor.getColumnIndex(MediaStore.MediaColumns.DISPLAY_NAME)
        return if (nameIndex >= 0) cursor.getString(nameIndex) else null
    }
}