package oleh.liskovych.gallerygif.utils

import android.util.Log
import oleh.liskovych.gallerygif.BuildConfig

object LOG {

    private val DEFAULT_TAG = ""
    private val DEFAULT_MESSAGE = ""

    fun e(tag: String = DEFAULT_TAG, message: String? = DEFAULT_MESSAGE, throwable: Throwable? = null) {
        if (isDebug) {
            throwable?.let { throwableUnwrapped ->
                message?.let { messageUnwrapped->
                    Log.e(tag, messageUnwrapped, throwableUnwrapped)
                }
            }
        }
    }

    private val isDebug: Boolean
        get() = BuildConfig.DEBUG

}