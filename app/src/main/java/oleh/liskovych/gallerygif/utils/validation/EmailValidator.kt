package oleh.liskovych.gallerygif.utils.validation

import android.content.Context
import android.text.TextUtils
import android.util.Patterns
import oleh.liskovych.gallerygif.R
import oleh.liskovych.gallerygif.utils.validation.common.ValidationResponse
import oleh.liskovych.gallerygif.utils.validation.common.ValidationResponseImpl
import oleh.liskovych.gallerygif.utils.validation.common.Validator

class EmailValidator private constructor(private val emptyError: String,
                                              private val invalidError: String) : Validator {

    companion object {
        fun builder(context: Context) = Builder(context)
    }

    override fun validate(email: String?): ValidationResponse {
        val error = if (email.isNullOrEmpty()) emptyError else if (!isEmailValid(email!!)) invalidError else ""
        return ValidationResponseImpl(error.isEmpty(), error)
    }


    private fun isEmailValid(email: String): Boolean {
        return !(email.isEmpty() || TextUtils.isDigitsOnly(email))
                && email.matches(Patterns.EMAIL_ADDRESS.toRegex())
    }

    class Builder(aContext: Context) {
        private val context = aContext.applicationContext
        private var emptyError: String = context.getString(R.string.email_is_empty)
        private var invalidError: String = context.getString(R.string.email_is_invalid)

        fun build() = EmailValidator(emptyError, invalidError)
    }

}

