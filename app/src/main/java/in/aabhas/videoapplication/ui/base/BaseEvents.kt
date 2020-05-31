package `in`.aabhas.videoapplication.ui.base

import android.content.Context

interface BaseEvents {

    val parentContext: Context

    fun onGeneralError(message: String)

    fun onGeneralError(exception: Exception)

    fun showErrorWithRetry(exception: Exception, call: () -> Unit)

    fun showErrorWithRetry(message: String, call: () -> Unit)

    fun showToast(message: String)
}
