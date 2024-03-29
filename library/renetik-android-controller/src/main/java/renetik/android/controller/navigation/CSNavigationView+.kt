package renetik.android.controller.navigation

import renetik.android.controller.base.CSActivityView

fun CSNavigationView.popLeave(count: Int) {
    while (controllers.size > count) pop()
}

val CSNavigationView.last: CSActivityView<*>? get() = controllers.lastOrNull()