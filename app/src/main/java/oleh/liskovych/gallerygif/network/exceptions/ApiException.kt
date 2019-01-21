package oleh.liskovych.gallerygif.network.exceptions

import oleh.liskovych.gallerygif.network.error.validation.ValidationErrorChildren

open class ApiException(): Exception() {

    open var statusCode: Int? = null
    var mMessage: String? = null
    var validationErrors: ValidationErrorChildren? = null

    constructor(statusCode: Int?,
                message: String?,
                validationErrors: ValidationErrorChildren?
                ) : this() {
        this.statusCode = statusCode
        this.mMessage = message
        this.validationErrors = validationErrors
    }

    override val message = mMessage
}