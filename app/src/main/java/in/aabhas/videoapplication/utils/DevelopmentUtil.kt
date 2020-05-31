package `in`.aabhas.videoapplication.utils

import `in`.aabhas.videoapplication.BuildConfig
import android.util.Log

object DevelopmentUtil {

}

object MLog {
    val debug: Boolean by lazy { BuildConfig.DEBUG }

    inline fun <reified T : Any> d(clazz: T, message: String) {
        if (debug) {
            Log.d(clazz::class.java.enclosingMethod?.name, message)
        }
    }

    inline fun <reified T : Any> d(clazz: T, exception: Exception) {
        if (debug) {
            Log.d(clazz::class.java.enclosingMethod?.name, "Exception!!")
            exception.printStackTrace()
        }
    }
}