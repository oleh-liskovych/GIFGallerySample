package oleh.liskovych.gallerygif.network.exceptions

import oleh.liskovych.gallerygif.App
import oleh.liskovych.gallerygif.R
import java.lang.Exception

class NoNetworkException : Exception() {

    companion object {
        private val ERROR_MESSAGE = App.instance.getString(R.string.no_internet_connection)
    }

    override val message = ERROR_MESSAGE
}