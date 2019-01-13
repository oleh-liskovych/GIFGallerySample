package oleh.liskovych.gallerygif.utils

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import oleh.liskovych.gallerygif.extensions.getUri
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*

object ImageUtils {

    private const val TYPE_FILE_IMAGE = "image/*"
    private const val DEFAULT_DATE_PATTERN = "yyyyMMdd_HHmmss"
    private const val IMAGE_FORMAT = ".jpg"
    private const val MAX_IMAGE_RESOLUTION = 1024
    private const val MAX_IMAGE_SIZE = 1024.0 * 1024.0
    private const val DEFAULT_IN_SAMPLE_SIZE = 1
    private const val RESOLUTION_REDUCTION_COEFFICIENT = 2
    private const val DEFAULT_COMPRESS_QUALITY = 90
    private const val FLAG_NOT_MODIFY_DATA_RETURN = 0
    private const val LOG_TAG = "ImageUtils"

    fun createImagePickIntentFromGallery(context: Context): Intent {
        Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI).run {
            context.packageManager.queryIntentActivities(this, FLAG_NOT_MODIFY_DATA_RETURN)?.let { if (it.isNotEmpty()) return this }
        }
        return Intent(Intent.ACTION_PICK).apply { type = TYPE_FILE_IMAGE }
    }

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

    private fun getImageSize(uri: Uri) = BitmapFactory.Options().apply {
        inJustDecodeBounds = true
        BitmapFactory.decodeFile(File(uri.path).absolutePath, this)
    }

    private fun decodeBitmapFromFile(file: File, targetWidth: Double, targetHeight: Double) =
            with(BitmapFactory.Options()) {
                inJustDecodeBounds = true
                BitmapFactory.decodeFile(file.path, this)
                inSampleSize = calculateInSampleSize(this, targetWidth, targetHeight)
                inJustDecodeBounds = false
                inPreferredConfig = Bitmap.Config.ARGB_8888
                BitmapFactory.decodeFile(file.path, this)
            }

    private fun calculateInSampleSize(options: BitmapFactory.Options, targetWidth: Double, targetHeight: Double): Int {
        val height = options.outHeight
        val width = options.outWidth
        var inSampleSize = DEFAULT_IN_SAMPLE_SIZE
        if (height > targetHeight || width > targetWidth) {
            do {
                inSampleSize *= RESOLUTION_REDUCTION_COEFFICIENT
            } while (height / inSampleSize >= targetHeight && width / inSampleSize >= targetWidth)
        }
        return inSampleSize
    }


    fun compressImageFromUri(imageUri: Uri, maxResolution: Int = MAX_IMAGE_RESOLUTION, maxSize: Double = MAX_IMAGE_SIZE): Bitmap? =
            File(imageUri.path).takeIf { it.exists() }?.let { file ->
                with(getImageSize(imageUri)) {
                    when {
                        file.length() > maxSize -> {
                            with(file.length() / maxSize) {
                                decodeBitmapFromFile(file, outWidth.toDouble() / this, outHeight.toDouble() / this)
                            }
                        }
                        Math.max(outWidth, outHeight) > maxResolution -> {
                            with(Math.max(outWidth, outHeight).toDouble() / maxResolution) {
                                decodeBitmapFromFile(file, outWidth / this, outHeight / this)
                            }
                        }
                        else -> BitmapFactory.decodeFile(file.path, null)
                    }
                }
            }



    fun saveBitmap(destinationFile: File?, bitmap: Bitmap?, quality: Int = DEFAULT_COMPRESS_QUALITY): Boolean {
        try {
            FileOutputStream(destinationFile).use { bitmap?.compress(Bitmap.CompressFormat.JPEG, quality, it) }
        } catch (e: Exception) {
            LOG.e(LOG_TAG, e.message)
            return false
        } finally {
            bitmap?.recycle()
        }
        return true
    }

    fun modifyImageToNormalOrientation(bitmap: Bitmap, path: String): Bitmap {
        val ei = ExifInterface(path)
        val orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)
        return when(orientation) {
            ExifInterface.ORIENTATION_ROTATE_90 -> rotate(bitmap, 90f)
            ExifInterface.ORIENTATION_ROTATE_180 -> rotate(bitmap, 180f)
            ExifInterface.ORIENTATION_ROTATE_270 -> rotate(bitmap, 270f)
            else -> bitmap
        }
    }

    private fun rotate(bitmap: Bitmap, degree: Float): Bitmap = with(Matrix()) {
        postRotate(degree)
        Bitmap.createBitmap(bitmap, FLAG_NOT_MODIFY_DATA_RETURN, FLAG_NOT_MODIFY_DATA_RETURN, bitmap.width, bitmap.height, this, true)
    }

}

























