package renetik.android.sample.model

import renetik.android.java.collections.list
import renetik.android.java.extensions.format
import java.text.DateFormat
import java.util.*

class SampleModel {
    val sampleList = list<SampleListRow>().putAll(
            SampleListRow("title 1", "sub title 1"), SampleListRow("title 2", "sub title 2"),
            SampleListRow("title 3", "sub title 3"), SampleListRow("title 4", "sub title 4"))
}

class SampleListRow(val title: String, val subtitle: String) {
    val time = Date().format(DateFormat.LONG, DateFormat.SHORT)
}
