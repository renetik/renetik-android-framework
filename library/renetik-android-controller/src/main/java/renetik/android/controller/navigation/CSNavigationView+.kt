package renetik.android.controller.navigation

import renetik.android.controller.base.CSActivityView

fun CSNavigation.popLeave(count: Int) {
    while (controllers.size > count) pop()
}

val CSNavigation.last: CSActivityView<*>? get() = controllers.lastOrNull()