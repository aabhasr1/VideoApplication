package `in`.aabhas.videoapplication.utils

import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.webkit.MimeTypeMap
import java.io.File

class GeneralUtils {

    companion object {

        @JvmStatic
        fun getRealPathFromURI(context: Context, contentUri: Uri?): String {
            var cursor: Cursor? = null
            return try {
                val proj =
                    arrayOf(MediaStore.Images.Media.DATA)
                cursor = context.contentResolver.query(contentUri!!, proj, null, null, null)
                val column_index = cursor!!.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
                cursor.moveToFirst()
                cursor.getString(column_index)
            } finally {
                cursor?.close()
            }
        }

        @JvmStatic
        fun getDurationFromVCLogRandomAccess(line: String): String? {
            var duration: String? = null
            val i1 = line.indexOf("Duration:")
            val i2 = line.indexOf(", start")
            if (i1 != -1 && i2 != -1) {
                duration = line.substring(i1 + 10, i2)
            }
            return duration
        }

        @JvmStatic
        fun readLastTimeFromVKLogUsingRandomAccess(line: String): String {
            var timeStr = "00:00:00.00"
            val i1 = line.indexOf("time=")
            val i2 = line.indexOf("bitrate=")
            if (i1 != -1 && i2 != -1) {
                timeStr = line.substring(i1 + 5, i2 - 1)
            } else if (line.startsWith("video:")) {
                timeStr = "exit"
            }
            return timeStr.trim { it <= ' ' }
        }

        fun getFile(context: Context, uri: Uri?): File? {
            if (uri != null) {
                val path = getPath(context, uri)
                if (path != null && isLocal(path)) {
                    return File(path)
                }
            }
            return null
        }

        fun getPath(context: Context, uri: Uri): String? {
            val isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT

            // DocumentProvider
            if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
                // LocalStorageProvider
                if (isExternalStorageDocument(uri)) {
                    val docId = DocumentsContract.getDocumentId(uri)
                    val split =
                        docId.split(":".toRegex()).toTypedArray()
                    val type = split[0]
                    if ("primary".equals(type, ignoreCase = true)) {
                        return Environment.getExternalStorageDirectory()
                            .toString() + "/" + split[1]
                    }

                    // TODO handle non-primary volumes
                } else if (isDownloadsDocument(uri)) {
                    val id = DocumentsContract.getDocumentId(uri)
                    var contentUri: Uri? = null
                    try {
                        contentUri = ContentUris.withAppendedId(
                            Uri.parse("content://downloads/public_downloads"),
                            java.lang.Long.valueOf(id!!)
                        )
                    } catch (e: Exception) {
                        if (id != null && id.contains("raw:")) {
                            return id.split("raw:".toRegex()).toTypedArray()[1]
                        }
                    }
                    return getDataColumn(context, contentUri, null, null)
                } else if (isMediaDocument(uri)) {
                    val docId = DocumentsContract.getDocumentId(uri)
                    val split =
                        docId.split(":".toRegex()).toTypedArray()
                    val type = split[0]
                    var contentUri: Uri? = null
                    if ("image" == type) {
                        contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                    } else if ("video" == type) {
                        contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                    } else if ("audio" == type) {
                        contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                    }
                    val selection = "_id=?"
                    val selectionArgs = arrayOf(
                        split[1]
                    )
                    return getDataColumn(
                        context,
                        contentUri,
                        selection,
                        selectionArgs
                    )
                }
            } else if ("content".equals(uri.scheme, ignoreCase = true)) {

                // Return the remote address
                return if (isGooglePhotosUri(uri)) uri.lastPathSegment else getDataColumn(
                    context,
                    uri,
                    null,
                    null
                )
            } else if ("file".equals(uri.scheme, ignoreCase = true)) {
                return uri.path
            }
            return null
        }

        fun getExtension(uri: String?): String? {
            if (uri == null) {
                return null
            }
            val dot = uri.lastIndexOf(".")
            return if (dot >= 0) {
                uri.substring(dot)
            } else {
                ""
            }
        }

        /**
         * @return Whether the URI is a local one.
         */
        fun isLocal(url: String?): Boolean {
            return if (url != null && !url.startsWith("http://") && !url.startsWith("https://")) {
                true
            } else false
        }

        /**
         * @return True if Uri is a MediaStore Uri.
         * @author paulburke
         */
        fun isMediaUri(uri: Uri): Boolean {
            return "media".equals(uri.authority, ignoreCase = true)
        }

        /**
         * Convert File into Uri.
         *
         * @param file
         * @return uri
         */
        fun getUri(file: File?): Uri? {
            return if (file != null) {
                Uri.fromFile(file)
            } else null
        }

        /**
         * @return The MIME type for the given file.
         */
        fun getMimeType(file: File): String? {
            val extension = getExtension(file.name)
            return if (extension!!.length > 0) {
                MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension.substring(1))
            } else "application/octet-stream"
        }

        /**
         * @return The MIME type for the give Uri.
         */
        fun getMimeType(context: Context, uri: Uri): String? {
            val file = File(getPath(context, uri))
            return getMimeType(file)
        }

        /**
         * @param uri The Uri to check.
         * @return Whether the Uri authority is ExternalStorageProvider.
         * @author paulburke
         */
        fun isExternalStorageDocument(uri: Uri): Boolean {
            return "com.android.externalstorage.documents" == uri.authority
        }

        /**
         * @param uri The Uri to check.
         * @return Whether the Uri authority is DownloadsProvider.
         * @author paulburke
         */
        fun isDownloadsDocument(uri: Uri): Boolean {
            return "com.android.providers.downloads.documents" == uri.authority
        }

        /**
         * @param uri The Uri to check.
         * @return Whether the Uri authority is MediaProvider.
         * @author paulburke
         */
        fun isMediaDocument(uri: Uri): Boolean {
            return "com.android.providers.media.documents" == uri.authority
        }

        /**
         * @param uri The Uri to check.
         * @return Whether the Uri authority is Google Photos.
         */
        fun isGooglePhotosUri(uri: Uri): Boolean {
            return "com.google.android.apps.photos.content" == uri.authority
        }

        /**
         * Get the value of the data column for this Uri. This is useful for
         * MediaStore Uris, and other file-based ContentProviders.
         *
         * @param context       The context.
         * @param uri           The Uri to query.
         * @param selection     (Optional) Filter used in the query.
         * @param selectionArgs (Optional) Selection arguments used in the query.
         * @return The value of the _data column, which is typically a file path.
         * @author paulburke
         */
        fun getDataColumn(
            context: Context, uri: Uri?, selection: String?,
            selectionArgs: Array<String>?
        ): String? {
            var cursor: Cursor? = null
            val column = "_data"
            val projection = arrayOf(
                column
            )
            try {
                cursor = context.contentResolver
                    .query(uri!!, projection, selection, selectionArgs, null)
                if (cursor != null && cursor.moveToFirst()) {
                    val column_index = cursor.getColumnIndexOrThrow(column)
                    return cursor.getString(column_index)
                }
            } finally {
                cursor?.close()
            }
            return null
        }
    }
}