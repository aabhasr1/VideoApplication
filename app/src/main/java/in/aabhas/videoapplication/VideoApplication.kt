package `in`.aabhas.videoapplication

import `in`.aabhas.videoapplication.di.viewModelModule
import `in`.aabhas.videoapplication.utils.MLog
import android.app.Application
import com.github.hiteshsondhi88.libffmpeg.FFmpeg
import com.github.hiteshsondhi88.libffmpeg.LoadBinaryResponseHandler
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegNotSupportedException
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin


class VideoApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(applicationContext)
            modules(viewModelModule)
        }
        setUpCompression()
    }

    private fun setUpCompression() {
        val ffMpeg = FFmpeg.getInstance(this)
        try {
            ffMpeg.loadBinary(object : LoadBinaryResponseHandler() {
                override fun onStart() {
                    MLog.d(this, "onStart")
                }

                override fun onFailure() {
                    MLog.d(this, "onFailure")
                }

                override fun onSuccess() {
                    MLog.d(this, "onSuccess")
                }

                override fun onFinish() {
                    MLog.d(this, "onFinish")
                }
            })
        } catch (e: FFmpegNotSupportedException) {
            // Handle if FFmpeg is not supported by device
            MLog.d(this, e)
        }
    }
}