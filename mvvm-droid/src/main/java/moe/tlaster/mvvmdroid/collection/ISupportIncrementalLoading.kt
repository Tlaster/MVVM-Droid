package moe.tlaster.mvvmdroid.collection

interface ISupportIncrementalLoading {
    suspend fun loadMoreItemsAsync()
    val hasMoreItems: Boolean
}


