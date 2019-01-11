package oleh.liskovych.gallerygif.utils

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Environment
import android.provider.MediaStore
import oleh.liskovych.gallerygif.extensions.getUri
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

object ImageUtils {

    private const val DEFAULT_DATE_PATTERN = "yyyyMMdd_HHmmss"
    private const val IMAGE_FORMAT = ".jpg"

    fun createImagePickIntentFromCamera(context: Context, outFile: () -> File, authority: String): Intent? =
        context.packageManager.takeIf { it.hasSystemFeature(PackageManager.FEATURE_CAMERA) }?.let {
            outFile().getUri(context, authority)
        }.let { uri ->
            Intent(MediaStore.ACTION_IMAGE_CAPTURE).takeIf { it.resolveActivity(context.packageManager) != null }
                ?.apply { putExtra(MediaStore.EXTRA_OUTPUT, uri) }
        }

    fun createImageFileTemp(context: Context, isPublic: Boolean, datePattern: String = DEFAULT_DATE_PATTERN): File {
        val timeStamp = SimpleDateFormat(datePattern, Locale.getDefault()).format(Date())
        val storageDir = if (isPublic) FileUtils.getAppDirectory(context) else context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        storageDir?.takeIf { !it.exists() }?.let { storageDir.mkdirs() }
        return File.createTempFile(timeStamp, IMAGE_FORMAT, storageDir)
    }

}