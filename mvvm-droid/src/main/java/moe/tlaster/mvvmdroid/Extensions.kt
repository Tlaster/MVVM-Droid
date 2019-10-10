package moe.tlaster.mvvmdroid

import android.content.res.Resources
import android.util.TypedValue
import kotlinx.coroutines.*

fun runOnMainThread(action: () -> Unit) {
    GlobalScope.runOnMainThread(action)
}

fun runOnIOThread(action: () -> Unit) {
    GlobalScope.runOnIOThread(action)
}

fun runOnDefaultThread(action: () -> Unit) {
    GlobalScope.runOnDefaultThread(action)
}


fun CoroutineScope.runOnMainThread(action: () -> Unit) {
    launch {
        withContext(Dispatchers.Main) {
            action.invoke()
        }
    }
}

fun CoroutineScope.runOnIOThread(action: () -> Unit) {
    launch {
        withContext(Dispatchers.IO) {
            action.invoke()
        }
    }
}

fun CoroutineScope.runOnDefaultThread(action: () -> Unit) {
    launch {
        withContext(Dispatchers.Default) {
            action.invoke()
        }
    }
}

inline fun async(noinline block: suspend () -> Unit): suspend () -> Unit = block

fun <T> (suspend () -> T).fireAndForgot() {
    GlobalScope.launch {
        this@fireAndForgot.invoke()
    }
}

internal val Number.dp: Float
    get() = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        this.toFloat(),
        Resources.getSystem().displayMetrics
    )