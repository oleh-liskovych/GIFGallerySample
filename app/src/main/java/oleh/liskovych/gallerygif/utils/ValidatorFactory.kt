package oleh.liskovych.gallerygif.utils

import android.content.Context
import oleh.liskovych.gallerygif.R
import oleh.liskovych.gallerygif.extensions.getIntegerRes
import oleh.liskovych.gallerygif.utils.validation.EmailValidator
import oleh.liskovych.gallerygif.utils.validation.PasswordValidator
import java.util.regex.Pattern

object ValidatorFactory {

    private val PASSWORD_PATTERN = Pattern.compile("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{6,}\$")

    fun emailValidator(context: Context) = with(context) {
        EmailValidator.builder(this)
            .emptyError(R.string.error_email_is_empty)
            .invalidError(R.string.error_email_is_invalid)
            .build()
    }

    fun passwordValidator(context: Context) = with(context) {
        PasswordValidator
            .builder(this)
            .passwordMinLength(getIntegerRes(R.integer.password_min_length))
            .passwordMaxLength(getIntegerRes(R.integer.password_max_length))
            .additionalRegex(PASSWORD_PATTERN)
            .build()
    }

}