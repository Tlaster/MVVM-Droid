package moe.tlaster.mvvmdroid.recyclerView

open class ItemSelector(val layoutId: Int, val bindingId: Int) :
    IItemSelector {
    override fun selectBindingVariable(item: Any?): Int {
        return bindingId
    }

    override fun selectLayout(item: Any?): Int {
        return layoutId
    }
}