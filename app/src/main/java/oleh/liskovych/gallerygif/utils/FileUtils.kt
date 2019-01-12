package oleh.liskovych.gallerygif.utils

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.os.Environment
import android.webkit.MimeTypeMap
import oleh.liskovych.gallerygif.R
import java.io.File
import java.io.InputStream
import java.util.*

object FileUtils {

    private const val DEFAULT_FOLDER_NAME = "PhotosCloudPictures"

    private fun File.copyInputStreamToFile(inputStream: InputStream) {
        inputStream.use { input ->
            this.outputStream().use { fileOut ->
                input.copyTo(fileOut)
            }
        }
    }

    fun pickedExistingPicture(context: Context, photoUri: Uri): File {
        val pictureInputStream = context.contentResolver.openInputStream(photoUri)
        val directory = tempImageDirectory(context)
        val photoFile = File(directory, UUID.randomUUID().toString() + "." + getMimeType(context, photoUri))
        photoFile.createNewFile()
        pictureInputStream?.let {
            photoFile.copyInputStreamToFile(it)
        }
        return photoFile
    }

    private fun tempImageDirectory(context: Context): File {
        val privateTempDir = File(context.cacheDir, DEFAULT_FOLDER_NAME)
        if (!privateTempDir.exists()) privateTempDir.mkdirs()
        return privateTempDir
    }

    private fun getMimeType(context: Context, uri: Uri): String? {
        return if (uri.scheme == ContentResolver.SCHEME_CONTENT) {
            val mime = MimeTypeMap.getSingleton()
            mime.getExtensionFromMimeType(context.contentResolver.getType(uri))
        } else {
            MimeTypeMap.getFileExtensionFromUrl(Uri.fromFile(File(uri.path)).toString())
        }
    }

    fun getAppDirectory(context: Context): File? =
        File(Environment.getExternalStorageDirectory(), context.getString(R.string.app_name)).apply {
            if (!exists()) mkdir()
        }
}
