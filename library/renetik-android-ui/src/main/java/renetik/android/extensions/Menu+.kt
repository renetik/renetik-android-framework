package renetik.android.extensions

import android.view.Menu
import android.view.MenuItem

fun Menu.item(id: Int): MenuItem = findItem(id)!!