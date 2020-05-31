package `in`.aabhas.videoapplication.utils

import android.content.Context
import android.os.Environment
import android.util.Log
import com.github.hiteshsondhi88.libffmpeg.FFmpeg
import com.github.hiteshsondhi88.libffmpeg.FFmpegExecuteResponseHandler
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegCommandAlreadyRunningException
import java.io.File

class VideoCompressor(private val context: Context) {
    private val mProgressCalculator: ProgressCalculator
    private var isFinished = false
    private var status = NONE
    private var errorMessage: String? = "Compression Failed!"
    fun startCompressing(
        inputPath: String?,
        compressionFormat: Constants.COMPRESSION_FORMATS,
        listener: CompressionListener?
    ) {
        if (inputPath == null || inputPath.isEmpty()) {
            status = NONE
            listener?.compressionFinished(NONE, false, null)
            return
        }
        var outputPath = ""
        outputPath = "$appDir/video_compress.mp4"
        val commandParams =
            arrayOf<String?>(
                "-y",
                "-i",
                inputPath,
                "-s",
                compressionFormat.resolution,
                "-b:v",
                compressionFormat.bitrate,
                "-c:a",
                "copy",
                outputPath
            )
        compressVideo(commandParams, outputPath, listener)
    }

    val appDir: String
        get() {
            var outputPath =
                Environment.getExternalStorageDirectory().absolutePath
            var file = File(outputPath)
            if (!file.exists()) {
                file.mkdir()
            }
            outputPath += "/" + "CompressedVideos"
            file = File(outputPath)
            if (!file.exists()) {
                file.mkdir()
            }
            return outputPath
        }

    private fun compressVideo(
        command: Array<String?>,
        outputFilePath: String,
        listener: CompressionListener?
    ) {
        try {
            FFmpeg.getInstance(context).execute(command, object : FFmpegExecuteResponseHandler {
                override fun onSuccess(message: String) {
                    Log.d("VideoCompressor", "onSuccess")
                    status = SUCCESS
                }

                override fun onProgress(message: String) {
                    status = RUNNING
                    Log.d("VideoCronProgress", "onProgress" + message)
                    var progress = mProgressCalculator.calcProgress(message)
                    Log.d("VideoCronProgress == ", "$progress..")
                    if (progress != 0 && progress <= 100) {
                        if (progress >= 99) {
                            progress = 100
                        }
                        listener!!.onProgress(progress)
                    }
                }

                override fun onFailure(message: String) {
                    status = FAILED
                    Log.d("VideoCompressor", "onFailure" + message)
                    listener?.onFailure("Error : $message")
                }

                override fun onStart() {
                    Log.d("VideoCompressor", "onStart")
                }

                override fun onFinish() {
                    Log.d("VideoCronProgress", "onFinish")
                    isFinished = true
                    listener?.compressionFinished(status, true, outputFilePath)
                }
            })
        } catch (e: FFmpegCommandAlreadyRunningException) {
            status = FAILED
            errorMessage = e.message
            listener?.onFailure("Error : " + e.message)
        }
    }

    interface CompressionListener {
        fun compressionFinished(
            status: Int,
            isVideo: Boolean,
            fileOutputPath: String?
        )

        fun onFailure(message: String?)
        fun onProgress(progress: Int)
    }

    val isDone: Boolean
        get() = status == SUCCESS || status == NONE

    companion object {
        const val SUCCESS = 1
        const val FAILED = 2
        const val NONE = 3
        const val RUNNING = 4
    }

    init {
        mProgressCalculator = ProgressCalculator()
    }
}