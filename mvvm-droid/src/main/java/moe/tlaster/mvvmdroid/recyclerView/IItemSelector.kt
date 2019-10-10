package moe.tlaster.mvvmdroid.recyclerView

interface IItemSelector {
    fun selectLayout(item: Any?): Int
    fun selectBindingVariable(item: Any?): Int
}