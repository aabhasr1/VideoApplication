package `in`.aabhas.videoapplication.utils

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar

object UiUtils {
    fun showToast(context: Context, message: String) {
        runOnUiThread {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }

    fun showSnackBarMessage(view: ViewGroup, message: String) {
        val sb = Snackbar.make(view, message, Snackbar.LENGTH_SHORT)
        sb.setTextColor(ContextCompat.getColor(view.context, android.R.color.white))
        sb.setBackgroundTint(ContextCompat.getColor(view.context, android.R.color.holo_blue_dark))
        sb.show()
    }

    fun showRetrySnackbar(view: View, msg: String, callBack: () -> Unit) {
        try {
            val snackbar = Snackbar.make(view, msg, Snackbar.LENGTH_INDEFINITE)
                .setAction("RETRY") {
                    callBack.invoke()
                }
                .setActionTextColor(ContextCompat.getColor(view.context, android.R.color.white))
                .setBackgroundTint(
                    ContextCompat.getColor(
                        view.context,
                        android.R.color.holo_blue_dark
                    )
                )
                .setTextColor(
                    ContextCompat.getColor(
                        view.context,
                        android.R.color.white
                    )
                )
                .setAnimationMode(Snackbar.ANIMATION_MODE_SLIDE)
            snackbar.show()
        } catch (e: Exception) {
        }
    }

    fun runOnUiThread(runnable: () -> Unit) {
        Handler(Looper.getMainLooper()).post { runnable() }
    }
}