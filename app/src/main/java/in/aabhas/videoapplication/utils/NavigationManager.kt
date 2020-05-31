package `in`.aabhas.videoapplication.utils

import `in`.aabhas.videoapplication.ui.screen2.Screen2Activity
import `in`.aabhas.videoapplication.ui.screen3.Screen3Activity
import `in`.aabhas.videoapplication.utils.Constants.IntentConstants.PICK_VIDEO_FROM_GALLERY
import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore


fun Activity.pickVideoFromGallery() {
    val pickIntent = Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI)
    startActivityForResult(pickIntent, PICK_VIDEO_FROM_GALLERY)
}

fun Activity.navigateToScreen2(videoURI: Uri) {
    launchActivity<Screen2Activity> {
        putExtra(Constants.IntentConstants.VIDEO_URI, videoURI)
    }
}

fun Activity.navigateToScreen3(videoURI: String) {
    launchActivity<Screen3Activity> {
        putExtra(Constants.IntentConstants.VIDEO_URI, videoURI)
    }
}

inline fun <reified T : ContextWrapper> Activity.launchActivity(
    requestCode: Int = -1,
    options: Bundle? = null,
    noinline init: Intent.() -> Unit = {}
) {

    val intent = newIntent<T>(this)
    intent.init()
    startActivityForResult(intent, requestCode, options)
}

inline fun <reified T : ContextWrapper> Context.launchActivity(
    options: Bundle? = null,
    noinline init: Intent.() -> Unit = {}
) {

    val intent = newIntent<T>(this)
    intent.init()
    startActivity(intent, options)
}

inline fun <reified T : ContextWrapper> newIntent(context: Context): Intent =
    Intent(context, T::class.java)