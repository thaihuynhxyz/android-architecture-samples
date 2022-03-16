package xyz.thaihuynh.android.sample.arch

interface BaseView<T> {
    fun setPresenter(presenter: T)
}