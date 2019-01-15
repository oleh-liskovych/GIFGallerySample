package oleh.liskovych.gallerygif.utils

import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

object NetworkUtils {

    private const val FORM_DATA_TYPE = "multipart/form-data"

    fun getMultipartFromFileUri(filePath: String, parameterName: String): MultipartBody.Part {
        val file = File(filePath)
        return getMultipartFromFileUri(file, parameterName)
    }

    fun getMultipartFromFileUri(file: File, parameterName: String): MultipartBody.Part {
        val filePart = RequestBody.create(MediaType.parse(FORM_DATA_TYPE), file)
        return MultipartBody.Part.createFormData(parameterName, file.name, filePart)
    }
}