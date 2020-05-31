package `in`.aabhas.videoapplication.ui.screen1

import `in`.aabhas.videoapplication.R
import `in`.aabhas.videoapplication.databinding.ActivityScreen1Binding
import `in`.aabhas.videoapplication.ui.base.BaseActivity
import `in`.aabhas.videoapplication.utils.Constants
import `in`.aabhas.videoapplication.utils.navigateToScreen2
import `in`.aabhas.videoapplication.utils.pickVideoFromGallery
import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat.requestPermissions
import androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale
import androidx.core.content.ContextCompat
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.koin.core.inject


class Screen1Activity : BaseActivity(), Screen1Events {
    private val model: Screen1ViewModel by inject()
    private val binding: ActivityScreen1Binding by lazyBinding()

    override val layoutResource = R.layout.activity_screen_1

    override fun getViewModel() = model

    override fun setBindings() {
        binding.model = model
    }

    var permissionDialog: AlertDialog? = null

    override fun initUi(savedInstanceState: Bundle?) {
    }

    override fun onResume() {
        super.onResume()
        askStoragePermission()
    }

    private fun askStoragePermission() {
        when {
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED -> {
                // You can use the API that requires the permission.
            }
            shouldShowRequestPermissionRationale(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) -> {
                if (permissionDialog != null) {
                    permissionDialog?.show()
                } else {
                    permissionDialog = MaterialAlertDialogBuilder(this)
                        .setTitle("Permission Required")
                        .setMessage("Write External Storage permission is required to stored the compressed video.")
                        .setCancelable(false)
                        .setPositiveButton(android.R.string.ok) { dialog, _ ->
                            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                            val uri =
                                Uri.fromParts("package", packageName, null)
                            intent.data = uri
                            startActivityForResult(intent, Constants.IntentConstants.PERMISSION_ASK)
                        }.create().apply {
                            show()
                        }
                }
            }
            else -> {
                // We can request the permission by launching the ActivityResultLauncher
                requestPermissions(
                    this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    Constants.IntentConstants.PERMISSION_ASK
                )
                // The registered ActivityResultCallback gets the result of the request.
            }
        }
    }

    override fun setEventHandler() {
        model.setEventHandler(this)
    }

    override fun fetchVideoFromGallery() {
        pickVideoFromGallery()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            Constants.IntentConstants.PICK_VIDEO_FROM_GALLERY -> {
                if (resultCode == Activity.RESULT_OK) {
                    val selectedMediaUri: Uri? = data?.data
                    selectedMediaUri?.toString()?.takeIf { it.contains("video") }?.let { videoUri ->
                        runOnUiThread { navigateToScreen2(selectedMediaUri) }
                    } ?: onGeneralError(Exception("The result was not a video"))
                }
            }
            Constants.IntentConstants.PERMISSION_ASK -> {
                askStoragePermission()
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }
}