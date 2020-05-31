package `in`.aabhas.videoapplication.utils

import `in`.aabhas.videoapplication.utils.GeneralUtils.Companion.getDurationFromVCLogRandomAccess
import `in`.aabhas.videoapplication.utils.GeneralUtils.Companion.readLastTimeFromVKLogUsingRandomAccess
import android.util.Log
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class ProgressCalculator {
    private var durationOfCurrent: String? = null
    private val simpleDateFormat: SimpleDateFormat
    private var timeRef: Long = -1
    private var prevProgress = 0
    fun initCalcParamsForNextInter() {
        durationOfCurrent = null
    }

    fun calcProgress(line: String?): Int {
        var progress = 0
        if (durationOfCurrent == null) {
            val dur = getDurationFromVCLogRandomAccess(line!!)
            if (!dur.isNullOrEmpty()) {
                durationOfCurrent = getDurationFromVCLogRandomAccess(line)
            }
        }
        if (durationOfCurrent != null) {
            val currentTimeStr =
                readLastTimeFromVKLogUsingRandomAccess(line!!)
            if (currentTimeStr == "exit") {
                prevProgress = 99
                return prevProgress
            }
            try {
                val durationCal = Calendar.getInstance()
                val durationDate = simpleDateFormat.parse(durationOfCurrent)
                durationCal.time = durationDate
                durationCal[Calendar.YEAR] = 2012
                val currentTimeCal = Calendar.getInstance()
                val currentTimeDate = simpleDateFormat.parse(currentTimeStr)
                currentTimeCal.time = currentTimeDate
                currentTimeCal[Calendar.YEAR] = 2012
                val durationLong = durationCal.timeInMillis - timeRef
                val currentTimeLong = currentTimeCal.timeInMillis - timeRef
                progress =
                    Math.round(currentTimeLong.toFloat() / durationLong * 100)
                if (prevProgress >= 99) {
                    progress = 99
                }
                prevProgress = progress
                Log.e(
                    COMPRESS_TAG,
                    "$prevProgress,.."
                )
            } catch (e: ParseException) {
                Log.w(COMPRESS_TAG, e.message)
            }
        }
        return progress
    }

    companion object {
        private const val COMPRESS_TAG = "Compress"
    }

    init {
        simpleDateFormat = SimpleDateFormat("HH:mm:ss.SS")
        initCalcParamsForNextInter()
        try {
            val c = Calendar.getInstance()
            val now = simpleDateFormat.parse("00:00:00.00")
            c.time = now
            c[Calendar.YEAR] = 2012
            timeRef = c.timeInMillis
        } catch (e: ParseException) {
            e.printStackTrace()
        }
    }
}