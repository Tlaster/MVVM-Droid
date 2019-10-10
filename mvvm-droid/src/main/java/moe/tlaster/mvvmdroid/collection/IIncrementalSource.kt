package moe.tlaster.mvvmdroid.collection

interface IIncrementalSource<T> {
    suspend fun getPagedItemAsync(page: Int, count: Int): List<T>
}