package `in`.aabhas.videoapplication.ui.screen2

import `in`.aabhas.videoapplication.ui.base.BaseViewModel
import `in`.aabhas.videoapplication.utils.GeneralUtils
import `in`.aabhas.videoapplication.utils.MLog
import `in`.aabhas.videoapplication.utils.VideoCompressor
import android.util.Log
import android.view.View
import androidx.databinding.ObservableField

class Screen2ViewModel : BaseViewModel<Screen2Events>() {

    val url = ObservableField<String>()
    val onCompressClick = View.OnClickListener {
        launchIO {
            url.get()?.let {
                Log.d(
                    "Actual Path",
                    GeneralUtils.getRealPathFromURI(event.parentContext, event.url)
                )
                VideoCompressor(event.parentContext).startCompressing(
                    GeneralUtils.getRealPathFromURI(event.parentContext, event.url),
                    object : VideoCompressor.CompressionListener {
                        override fun compressionFinished(
                            status: Int,
                            isVideo: Boolean,
                            fileOutputPath: String?
                        ) {
                            event.onCompressionEnd(fileOutputPath)
                        }

                        override fun onFailure(message: String?) {
                            message?.let {
                                MLog.d(this, Exception(message))
                                event.onGeneralError(it)
                                event.onCompressionEnd(null)
                            }
                        }

                        override fun onProgress(progress: Int) {
                            event.onCompressionProgress(progress)
                        }

                    })
            }
        }
    }
}