package renetik.android.core.android.content

import android.content.ContentResolver
import android.net.Uri
import android.provider.OpenableColumns.DISPLAY_NAME

//fun ContentResolver.getContentName(uri: Uri): String? {
//    query(uri, null, null, null, null).use { cursor ->
//        cursor!!.moveToFirst()
//        val nameIndex = cursor.getColumnIndex(MediaStore.MediaColumns.DISPLAY_NAME)
//        return if (nameIndex >= 0) cursor.getString(nameIndex) else null
//    }
//}

fun ContentResolver.getDisplayName(uri: Uri): String? =
    query(uri, null, null, null, null)?.use {
        it.moveToFirst()
        val nameIndex = it.getColumnIndex(DISPLAY_NAME)
        if (nameIndex >= 0) it.getString(nameIndex) else null
    }

//fun ContentResolver.getDisplayName(uri: Uri): String? =
//    query(uri, null, null, null, null)?.use {
//        val nameIndex = it.getColumnIndex(DISPLAY_NAME)
//        it.moveToFirst()
//        it.getString(nameIndex)
//    }

