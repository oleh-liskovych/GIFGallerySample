package oleh.liskovych.gallerygif.utils

import android.content.Context
import android.os.Environment
import oleh.liskovych.gallerygif.R
import java.io.File

object FileUtils {

    fun getAppDirectory(context: Context): File? = File(Environment.getExternalStorageDirectory(), context.getString(R.string.app_name)).apply {
        if (!exists()) mkdir()
    }
}