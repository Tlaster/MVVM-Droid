package moe.tlaster.mvvmdroid.recyclerView

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import moe.tlaster.mvvmdroid.collection.CollectionChangedEventArg
import moe.tlaster.mvvmdroid.collection.ISupportIncrementalLoading
import moe.tlaster.mvvmdroid.collection.ObservableCollection
import moe.tlaster.mvvmdroid.runOnMainThread

open class DataBindingAdapter<T>(var itemSelector: IItemSelector) :
    RecyclerView.Adapter<DataBindingViewHolder>() {

    var onClick: ((T) -> Unit)? = null
    private val onCollectionChanged: (Any, CollectionChangedEventArg) -> Unit = { _, _ ->
        runOnMainThread {
            notifyDataSetChanged()
        }
    }
    private lateinit var inflater: LayoutInflater

    var items: List<T> = emptyList()
        set(value) {
            val current = field
            if (current is ObservableCollection) {
                current.collectionChanged -= onCollectionChanged
            }
            field = value
            if (value is ObservableCollection) {
                value.collectionChanged += onCollectionChanged
            }
            if (value is ISupportIncrementalLoading && !value.any()) {
                GlobalScope.launch {
                    value.loadMoreItemsAsync()
                }
            }
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataBindingViewHolder {

        if (!this::inflater.isInitialized) {
            inflater = LayoutInflater.from(parent.context)
        }

        val binding = DataBindingUtil.inflate<ViewDataBinding>(inflater, viewType, parent, false)
        return DataBindingViewHolder(binding)
    }

    override fun getItemViewType(position: Int): Int {
        val item = getItemAt(position)
        return itemSelector.selectLayout(item)
    }

    override fun getItemCount(): Int {
        return items.count()
    }

    override fun onBindViewHolder(viewHolder: DataBindingViewHolder, position: Int) {
        val item = getItemAt(position)
        viewHolder.binding.root.setOnClickListener {
            onClick?.invoke(item)
        }
        viewHolder.binding.setVariable(itemSelector.selectBindingVariable(item), item)
        viewHolder.binding.executePendingBindings()
    }

    private var isLoading = false
    private fun getItemAt(position: Int): T {
        val currentItems = items
        if (isNearEnd(position, currentItems.size) &&
            currentItems is ISupportIncrementalLoading &&
            currentItems.hasMoreItems &&
            !isLoading
        ) {
            GlobalScope.launch {
                isLoading = true
                currentItems.loadMoreItemsAsync()
                isLoading = false
            }
        }
        return currentItems[position]
    }

    protected open fun isNearEnd(index: Int, count: Int): Boolean {
        return index == count - 1
    }

}