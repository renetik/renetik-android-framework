package renetik.android.sample.model

import renetik.android.java.collections.list
import renetik.android.java.format
import java.text.DateFormat
import java.util.*

class SampleModel {
    val sampleList = list<SampleListItem>().append(
            SampleListItem("title 1", "sub title 1"), SampleListItem("title 2", "sub title 2"),
            SampleListItem("title 3", "sub title 3"), SampleListItem("title 4", "sub title 4"))
}

class SampleListItem(val title: String, val subtitle: String) {
    val time = Date().format(DateFormat.LONG, DateFormat.SHORT)
}
