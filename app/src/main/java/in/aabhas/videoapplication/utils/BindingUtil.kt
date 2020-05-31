package `in`.aabhas.videoapplication.utils

import android.widget.VideoView
import androidx.databinding.BindingAdapter

object BindingUtil {

    @JvmStatic
    @BindingAdapter("urlLoop")
    fun VideoView.setAndPlayInLoop(url: String?) {
        url?.let { url ->
            setVideoPath(url)
            setOnPreparedListener {
                it.isLooping = true
            }
            setOnErrorListener { mp, what, extra ->
                MLog.d(this, "Error Listener")
                false
            }
            start()
        }
    }

    @JvmStatic
    @BindingAdapter("url")
    fun VideoView.setAndPlay(url: String?) {
        url?.let { url ->
            setVideoPath(url)
            setOnErrorListener { mp, what, extra ->
                MLog.d(this, "Error Listener")
                false
            }
            start()
        }
    }
}
