package oleh.liskovych.gallerygif.network.exceptions

import oleh.liskovych.gallerygif.App
import oleh.liskovych.gallerygif.R

class ServerException : ApiException() {

    companion object {
        private val ERROR_MESSAGE = App.instance.getString(R.string.server_error)
    }

    override var statusCode: Int? = 500
    override val message = ERROR_MESSAGE
}