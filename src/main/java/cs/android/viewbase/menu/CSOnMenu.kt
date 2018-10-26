package cs.android.viewbase.menu

import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import cs.java.collections.CSList
import cs.java.lang.CSLang.*
import cs.java.lang.CSValue

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
        }
    }

    fun inflate(menuId: Int) = activity.menuInflater.inflate(menuId, menu)

    fun <T : CSMenuItem> onPrepareItems(menuItems: CSList<T>) {
        for (item in menuItems) if (item.isVisible) show(item)
    }

}