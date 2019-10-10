package moe.tlaster.mvvmdroid.bindingAdapter

import androidx.databinding.BindingAdapter
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import moe.tlaster.mvvmdroid.runOnMainThread

@BindingAdapter("onRefresh")
fun onRefresh(swipeRefreshLayout: SwipeRefreshLayout, action: suspend () -> Unit) {
    val refreshAction = {
        GlobalScope.launch {
            runOnMainThread {
                swipeRefreshLayout.isRefreshing = true
            }
            action.invoke()
            runOnMainThread {
                swipeRefreshLayout.isRefreshing = false
            }
        }
    }
    swipeRefreshLayout.setOnRefreshListener {
        refreshAction.invoke()
    }
//    refreshAction.invoke()//initial refresh
}

