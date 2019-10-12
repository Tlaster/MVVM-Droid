package moe.tlaster.mvvmdroid.bindingAdapter

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import moe.tlaster.mvvmdroid.recyclerView.DataBindingAdapter
import moe.tlaster.mvvmdroid.recyclerView.ItemSelector


@BindingAdapter("itemsSource", "itemTemplate", requireAll = true)
fun <T> adapter(recyclerView: RecyclerView, list: List<T>?, layoutId: Int) {
    val onClick = recyclerView.getTag(ITEMCLICK_ID) as? (T) -> Unit
    val id = Class.forName("${recyclerView.context.applicationContext.packageName}.BR").getField("model").get(null) as Int
    recyclerView.adapter = DataBindingAdapter<T>(ItemSelector(layoutId, id)).also { adapter ->
        list?.let {
            adapter.items = list
        }
        onClick?.let {
            adapter.onClick = it
        }
    }
}


private val ITEMCLICK_ID = -4590
@BindingAdapter("itemClicked")
fun <T> itemClicked(recyclerView: RecyclerView, action: (T) -> Unit) {
    val adapter = recyclerView.adapter?.let {
        it as? DataBindingAdapter<T>
    }
    if (adapter != null) {
        adapter.onClick = action
    } else {
        recyclerView.setTag(ITEMCLICK_ID, action)
    }
}
