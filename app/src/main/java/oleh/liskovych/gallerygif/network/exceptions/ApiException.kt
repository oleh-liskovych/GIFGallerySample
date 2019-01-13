package oleh.liskovych.gallerygif.network.exceptions

open class ApiException(): Exception() {

    open var statusCode: Int? = null
    var mMessage: String? = null

    constructor(statusCode: Int?,
                v: String?,
                message: String?) : this() {
        this.statusCode = statusCode
        this.mMessage = message
    }

    override val message = mMessage
}