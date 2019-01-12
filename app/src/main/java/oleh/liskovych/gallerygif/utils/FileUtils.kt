package oleh.liskovych.gallerygif.utils

import android.content.Context
import android.net.Uri
import android.os.Environment
import oleh.liskovych.gallerygif.R
import java.io.File

object FileUtils {

    private const val CONTENT = "content"
    private const val COLUMN_DATA = "_data"

    fun getContentPath(context: Context, uri: Uri): String? {
        return if (CONTENT.equals(uri.scheme, ignoreCase = true)) {
            getDataColumn(context, uri, null, null)
        } else null
    }

    private fun getDataColumn(context: Context, uri: Uri, selection: String? = null, selectionArgs: Array<String>? = null): String? =
        context.contentResolver.query(uri, arrayOf(COLUMN_DATA), selection, selectionArgs, null)?.use { cursor ->
            cursor.takeIf { cursor.moveToFirst() }?.getString(cursor.getColumnIndexOrThrow(COLUMN_DATA))
        }

    fun getAppDirectory(context: Context): File? =
        File(Environment.getExternalStorageDirectory(), context.getString(R.string.app_name)).apply {
            if (!exists()) mkdir()
        }
}

