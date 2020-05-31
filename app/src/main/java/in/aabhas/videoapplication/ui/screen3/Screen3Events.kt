package `in`.aabhas.videoapplication.ui.screen3

import `in`.aabhas.videoapplication.ui.base.BaseEvents

interface Screen3Events : BaseEvents {
    fun onVideoPause()
    fun onVideoPlay()
    val isPlaying: Boolean
}