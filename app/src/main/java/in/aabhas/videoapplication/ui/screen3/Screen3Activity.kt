package `in`.aabhas.videoapplication.ui.screen3

import `in`.aabhas.videoapplication.R
import `in`.aabhas.videoapplication.databinding.ActivityScreen3Binding
import `in`.aabhas.videoapplication.ui.base.BaseActivity
import `in`.aabhas.videoapplication.utils.Constants
import android.os.Bundle
import org.koin.core.inject


class Screen3Activity : BaseActivity(), Screen3Events {
    private val model: Screen3ViewModel by inject()
    private val binding: ActivityScreen3Binding by lazyBinding()

    override val layoutResource = R.layout.activity_screen_3
    override fun getViewModel() = model

    override val isPlaying: Boolean get() = binding.videoView.isPlaying

    private val url: String? by lazy { intent.extras?.getString(Constants.IntentConstants.VIDEO_URI) }

    override fun setBindings() {
        binding.model = model
        url?.let {
            model.url.set(it)
        }
    }

    override fun onPause() {
        binding.videoView.pause()
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
        binding.videoView.resume()
    }

    override fun initUi(savedInstanceState: Bundle?) {

    }

    override fun onVideoPause() {
        binding.videoView.pause()
    }

    override fun onVideoPlay() {
        binding.videoView.resume()
    }

    override fun setEventHandler() {
        model.setEventHandler(this)
    }
}