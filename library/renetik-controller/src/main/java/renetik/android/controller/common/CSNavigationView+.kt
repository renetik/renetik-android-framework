package renetik.android.controller.common

fun CSNavigationView.popLeave(count: Int) {
    while (controllers.size > count) pop()
}