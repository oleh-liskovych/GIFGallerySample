package oleh.liskovych.gallerygif.utils.validation

import android.content.Context
import androidx.annotation.StringRes
import oleh.liskovych.gallerygif.R
import oleh.liskovych.gallerygif.utils.validation.common.ValidationResponse
import oleh.liskovych.gallerygif.utils.validation.common.ValidationResponseImpl
import oleh.liskovych.gallerygif.utils.validation.common.Validator
import java.util.regex.Pattern

// there is some redundant code in the class. It was added intently for demonstration
class PasswordValidator private constructor(private val emptyErrorMessage: String,
                                            private val invalidErrorMessage: String,
                                            private val minLengthErrorMessage: String,
                                            private val maxLengthErrorMessage: String,
                                            private val passwordMinLength: Int = 0,
                                            private val passwordMaxLength: Int = 0,
                                            private val additionalRegex: Regex? = null) : Validator {

    companion object {

        fun builder(context: Context) = Builder(context)

        fun getDefaultValidator(context: Context) = builder(context).build()

    }

    override fun validate(password: String?): ValidationResponse {
        val error = if (password.isNullOrEmpty()) {
            emptyErrorMessage
        } else if (password!!.length < passwordMinLength) {
            minLengthErrorMessage
        } else if (password.length > passwordMaxLength) {
            maxLengthErrorMessage
        } else if (!isPasswordValid(password)) {
            invalidErrorMessage
        } else {
            ""
        }
        return ValidationResponseImpl(error.isEmpty(), error)
    }

    private fun isPasswordValid(password: String) = !password.isEmpty()
            && password.length >= passwordMinLength
            && password.length <= passwordMaxLength
            && checkPatternAndValidate(password)

    private fun checkPatternAndValidate(password: String) = additionalRegex?.matches(password) ?: true

    class Builder(aContext: Context) {
        private val context = aContext.applicationContext
        var emptyErrorMessage: String = context.getString(R.string.password_is_empty)
        var invalidErrorMessage: String = context.getString(R.string.password_is_invalid)

        var passwordMinLength: Int = context.resources.getInteger(R.integer.password_min_length)
        var passwordMaxLength: Int = context.resources.getInteger(R.integer.password_max_length)

        var minLengthErrorMessage: String? = null
        var maxLengthErrorMessage: String? = null
        var additionalRegex: Regex? = null

        fun emptyErrorMessage(@StringRes id: Int) = apply { emptyErrorMessage = context.getString(id) }
        fun invalidErrorMessage(@StringRes id: Int) = apply { invalidErrorMessage = context.getString(id) }
        fun minLengthErrorMessage(@StringRes id: Int) = apply { minLengthErrorMessage = context.getString(id) }
        fun maxLengthErrorMessage(@StringRes id: Int) = apply { maxLengthErrorMessage = context.getString(id) }
        fun passwordMinLength(length: Int?) = apply { if (length != null && length > 0) passwordMinLength = length }
        fun passwordMaxLength(length: Int?) = apply { if (length != null && length > 0) passwordMaxLength = length }

        fun additionalRegex(pattern: Pattern) = apply { additionalRegex = pattern.toRegex() }
        fun additionalRegex(pattern: String) = apply { additionalRegex = pattern.toRegex() }
        fun additionalRegex(pattern: Regex) = apply { additionalRegex = pattern }

        fun build(): Validator {
            passwordMaxLength = Math.max(passwordMinLength, passwordMaxLength)

            if (minLengthErrorMessage == null) {
                minLengthErrorMessage = String.format(context.getString(R.string.password_min_length_error),
                    passwordMinLength)
            }

            if (maxLengthErrorMessage == null) {
                maxLengthErrorMessage = String.format(context.getString(R.string.password_max_length_error),
                    passwordMaxLength)
            }

            return PasswordValidator(emptyErrorMessage, invalidErrorMessage, minLengthErrorMessage ?: "",
                maxLengthErrorMessage ?: "", passwordMinLength, passwordMaxLength, additionalRegex)
        }
    }
}
