package oleh.liskovych.gallerygif.utils.validation

import android.content.Context
import android.text.TextUtils
import android.util.Patterns
import androidx.annotation.StringRes
import oleh.liskovych.gallerygif.R
import oleh.liskovych.gallerygif.utils.validation.common.ValidationResponse
import oleh.liskovych.gallerygif.utils.validation.common.ValidationResponseImpl
import oleh.liskovych.gallerygif.utils.validation.common.Validator

// there is some redundant code in the class. It was added intently for demonstration
class EmailValidator private constructor(private val emptyError: String,
                                         private val invalidError: String,
                                         private val additionalRegex: Regex?) : Validator {

    companion object {
        fun builder(context: Context) = Builder(context)

        fun getDefaultValidator(context: Context) = builder(context).build()
    }

    override fun validate(email: String?): ValidationResponse {
        val error = if (email.isNullOrEmpty()) emptyError else if (!isEmailValid(email!!)) invalidError else ""
        return ValidationResponseImpl(error.isEmpty(), error)
    }


    private fun isEmailValid(email: String): Boolean {
        return !(email.isEmpty() || TextUtils.isDigitsOnly(email))
                && email.matches(Patterns.EMAIL_ADDRESS.toRegex())
                && additionalCheck(email)
    }

    private fun additionalCheck(email: String) = additionalRegex?.matches(email) ?: true

    class Builder(aContext: Context) {
        private val context = aContext.applicationContext
        private var emptyError: String = context.getString(R.string.default_error_email_is_empty)
        private var invalidError: String = context.getString(R.string.default_error_email_is_invalid)
        var additionalRegex: Regex? = null

        fun emptyError(@StringRes emptyError: Int) = apply {
            this.emptyError = context.getString(emptyError)
        }

        fun invalidError(@StringRes invalidError: Int) = apply {
            this.invalidError = context.getString(invalidError)
        }

        fun additionalRegex(additionalRegex: Regex?) = apply { this.additionalRegex = additionalRegex }

        fun build() = EmailValidator(emptyError, invalidError, additionalRegex)
    }

}

