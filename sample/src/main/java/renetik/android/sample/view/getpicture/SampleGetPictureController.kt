package renetik.android.sample.view.getpicture

import android.view.View
import android.widget.GridView
import renetik.android.base.layout
import renetik.android.controller.base.CSViewController
import renetik.android.controller.common.CSNavigationItem
import renetik.android.dialog.extensions.dialog
import renetik.android.dialog.showView
import renetik.android.extensions.imageView
import renetik.android.extensions.textView
import renetik.android.getpicture.CSGetPictureController
import renetik.android.listview.CSListController
import renetik.android.listview.CSRowView
import renetik.android.listview.actions.CSRemoveListRowsController
import renetik.android.material.extensions.floatingButton
import renetik.android.sample.R
import renetik.android.sample.model.ImageItem
import renetik.android.sample.model.model
import renetik.android.sample.view.navigation
import renetik.android.view.extensions.image
import renetik.android.view.extensions.imageView
import renetik.android.view.extensions.onClick
import renetik.android.view.extensions.title

class SampleGetPictureController(title: String)
    : CSViewController<View>(navigation, layout(R.layout.sample_getpicture)), CSNavigationItem {

    private val grid = CSListController<ImageItem, GridView>(this, R.id.SampleGetPicture_Grid) {
        CSRowView(this, layout(R.layout.sample_getpicture_item)) { data ->
            imageView(R.id.SampleGetPictureItem_Image).image(data.image.value)
        }
    }.onItemClick { row ->
        dialog("Image detail").showView(R.layout.sample_getpicture_item)
                .imageView(R.id.SampleGetPictureItem_Image).image(row.data.image.value)
    }.emptyView(R.id.SampleGetPicture_ListEmpty)

    private val getPicture = CSGetPictureController(this, "Select photo or take picture", "Pictures") {
        model.getPictureList.add(ImageItem(it))
        model.save()
        reloadGrid()
    }

    init {
        textView(R.id.SampleGetPicture_Title).title(title)
        CSRemoveListRowsController(grid, "Remove selected items ?") { toRemove ->
            toRemove.forEach { item -> model.getPictureList.remove(item) }
            model.save()
            grid.reload(model.getPictureList.list)
        }
        floatingButton(R.id.SampleGetPicture_AddImageButton).onClick { getPicture.show() }
        reloadGrid()
    }

    private fun reloadGrid() = grid.reload(model.getPictureList.list)
}