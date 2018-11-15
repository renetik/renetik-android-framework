package renetik.android.viewbase.menu

import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import renetik.android.java.collections.CSList
import renetik.android.java.lang.CSValue
import renetik.android.lang.CSLang.YES
import renetik.android.lang.CSLang.set

const val GeneratedMenuItems = 7687678

class CSOnMenu(private val activity: AppCompatActivity, val menu: Menu) {

    val showMenu = CSValue(true)

    fun find(id: Int): MenuItem? = menu.findItem(id)

    fun show(item: CSMenuItem) {
        if (item.isGenerated) createProgrammaticallyItem(item)
        else find(item.id)?.apply {
            isVisible = YES
            item.title?.let { title = it }
            if (isCheckable) item.isChecked?.let { isChecked = it }
                    ?: let { item.isChecked = isChecked }
            actionView?.let { item.actionView }
        }
    }

    private fun createProgrammaticallyItem(item: CSMenuItem) {
        menu.add(GeneratedMenuItems, item.id, Menu.NONE, item.title).apply {
            setShowAsAction(item.showAsAction)
            if (set(item.iconResource)) setIcon(item.iconResource)
            item.isChecked?.let {
                isCheckable = true
                isChecked = it
            }
            item.actionView?.let { actionView = it }
        }
    }

    fun inflate(menuId: Int) = activity.menuInflater.inflate(menuId, menu)

    fun <T : CSMenuItem> onPrepareItems(menuItems: CSList<T>) {
        for (item in menuItems) if (item.isVisible) show(item)
    }

}