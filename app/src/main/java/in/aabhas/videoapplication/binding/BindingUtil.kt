package `in`.aabhas.videoapplication.binding

import `in`.aabhas.videoapplication.utils.MLog
import android.widget.Spinner
import android.widget.VideoView
import androidx.databinding.BindingAdapter
import kotlinx.android.synthetic.main.activity_screen_2.view.*

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

    @JvmStatic
    @BindingAdapter("configurationWithAdapter")
    fun Spinner.setConfiguration(spinnerConfiguration: SpinnerConfiguration?) {
        spinnerConfiguration?.let {
            spinner.adapter = it.adapter
            it.onSpinnerConfig(this)
        }
    }
}
