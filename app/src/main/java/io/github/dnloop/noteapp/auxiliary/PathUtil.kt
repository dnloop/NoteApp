package io.github.dnloop.noteapp.auxiliary

import android.annotation.SuppressLint
import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.provider.DocumentsContract
import android.provider.MediaStore
import androidx.core.content.ContextCompat
import java.io.File
import java.net.URISyntaxException

object PathUtil {
    /**
     * Gets the file path of the given Uri.
     */
    @SuppressLint("NewApi")
    @Throws(URISyntaxException::class)
    fun getPath(context: Context, uri: Uri): String? {
        var documentUri: Uri = uri
        val needToCheckUri = Build.VERSION.SDK_INT >= 19
        var selection: String? = null
        var selectionArgs: Array<String>? = null
        // Uri is different in versions after KITKAT (Android 4.4), we need to
        // deal with different Uris.
        if (needToCheckUri && DocumentsContract.isDocumentUri(
                context.applicationContext,
                documentUri
            )
        ) {
            when {
                isExternalStorageDocument(documentUri) -> {
                    val docId = DocumentsContract.getDocumentId(documentUri)
                    val docIdSplit = docId.split(":").toTypedArray()
                    val externalStorageVolumes: Array<out File> =
                        ContextCompat.getExternalFilesDirs(context.applicationContext, null)
                    val primaryExternalStorage = externalStorageVolumes[0]
                    val extStoSplit = primaryExternalStorage.path.split("/").toTypedArray()
                    val path = "/"+extStoSplit[1]+"/"+extStoSplit[2]+"/"
                    return path + "/" + docIdSplit[1]
                }
                isDownloadsDocument(documentUri) -> {
                    val id = DocumentsContract.getDocumentId(documentUri)
                    documentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), java.lang.Long.valueOf(id)
                    )
                }
                isMediaDocument(documentUri) -> {
                    val docId = DocumentsContract.getDocumentId(documentUri)
                    val split = docId.split(":").toTypedArray()
                    when (split[0]) {
                        "image" -> {
                            documentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                        }
                        "video" -> {
                            documentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                        }
                        "audio" -> {
                            documentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                        }
                    }
                    selection = "_id=?"
                    selectionArgs = arrayOf(split[1])
                }
            }
        }
        if ("content".equals(documentUri.scheme, ignoreCase = true)) {
            val projection =
                arrayOf(MediaStore.Images.Media._ID)
            val cursor: Cursor?
            try {
                cursor = context.contentResolver
                    .query(documentUri, projection, selection, selectionArgs, null)
                val columnIndex: Int = cursor!!.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
                if (cursor.moveToFirst()) {
                    return cursor.getString(columnIndex)
                }
                cursor.close()
            } catch (e: Exception) {
            }
        } else if ("file".equals(documentUri.scheme, ignoreCase = true)) {
            return documentUri.path
        }
        return null
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    private fun isExternalStorageDocument(uri: Uri): Boolean {
        return "com.android.externalstorage.documents" == uri.authority
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    private fun isDownloadsDocument(uri: Uri): Boolean {
        return "com.android.providers.downloads.documents" == uri.authority
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    private fun isMediaDocument(uri: Uri): Boolean {
        return "com.android.providers.media.documents" == uri.authority
    }
}
