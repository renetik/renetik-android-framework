package renetik.android.extensions.view

import android.content.res.ColorStateList
import android.widget.ImageView
import com.bumptech.glide.Glide
import java.io.File

fun <T : ImageView> T.image(file: File) = apply {
    Glide.with(this).load(file).into(this)
}

fun <T : ImageView> T.image(url: String) = apply {
    Glide.with(this).load(url).into(this)
}

fun <T : ImageView> T.image(resourceId: Int) = apply {
    Glide.with(this).load(resourceId).into(this)
    //I could not use glide because it dous not load resource immediatelly
    // and when I want to assign icon tnt after it just crash on null pointer exception
//    setImageResource(resourceId)
}

fun <T : ImageView> T.iconTint(color: Int) = apply {
    imageTintList = ColorStateList.valueOf(color)
}