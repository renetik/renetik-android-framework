package renetik.android.ui.extensions.view

import android.view.Menu
import android.view.MenuItem

fun Menu.item(id: Int): MenuItem = findItem(id)!!