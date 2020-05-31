package `in`.aabhas.videoapplication.ui.screen1

import `in`.aabhas.videoapplication.R
import `in`.aabhas.videoapplication.databinding.ActivityScreen1Binding
import `in`.aabhas.videoapplication.ui.base.BaseActivity
import `in`.aabhas.videoapplication.utils.Constants
import `in`.aabhas.videoapplication.utils.navigateToScreen2
import `in`.aabhas.videoapplication.utils.pickVideoFromGallery
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import org.koin.core.inject


class Screen1Activity : BaseActivity(), Screen1Events {
    private val model: Screen1ViewModel by inject()
    private val binding: ActivityScreen1Binding by lazyBinding()

    override val layoutResource = R.layout.activity_screen_1

    override fun getViewModel() = model

    override fun setBindings() {
        binding.model = model
    }

    override fun initUi(savedInstanceState: Bundle?) {
    }

    override fun setEventHandler() {
        model.setEventHandler(this)
    }

    override fun fetchVideoFromGallery() {
        pickVideoFromGallery()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK && requestCode == Constants.IntentConstants.PICK_VIDEO_FROM_GALLERY) {
            val selectedMediaUri: Uri? = data?.data
            selectedMediaUri?.toString()?.takeIf { it.contains("video") }?.let { videoUri ->
                runOnUiThread { navigateToScreen2(selectedMediaUri) }
            } ?: onGeneralError(Exception("The result was not a video"))
        }
        super.onActivityResult(requestCode, resultCode, data)
    }
}