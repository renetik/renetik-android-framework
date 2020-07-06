package renetik.android.view.extensions

import android.view.Menu
import android.view.MenuItem

fun Menu.item(id: Int): MenuItem = findItem(id)!!