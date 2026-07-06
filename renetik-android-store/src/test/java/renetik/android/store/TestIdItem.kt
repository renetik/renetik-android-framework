package renetik.android.store

import renetik.android.core.lang.CSHasId

enum class TestIdItem(override val id: String) : CSHasId {
    First("id1"), Second("id2"), Third("id3"), Fourth("id4");

    companion object {
        val TestIdItems = entries
    }
}