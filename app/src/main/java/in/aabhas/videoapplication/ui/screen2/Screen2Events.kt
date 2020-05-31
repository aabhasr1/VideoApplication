package `in`.aabhas.videoapplication.ui.screen2

import `in`.aabhas.videoapplication.ui.base.BaseEvents
import android.net.Uri

interface Screen2Events : BaseEvents {
    val url: Uri?
    fun onCompressionStart()
    fun onCompressionEnd(outputFilePath: String?)
    fun onCompressionProgress(progress: Int)
}