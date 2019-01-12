package oleh.liskovych.gallerygif.utils.validation

import android.content.Context
import androidx.annotation.StringRes
import oleh.liskovych.gallerygif.R
import oleh.liskovych.gallerygif.utils.validation.common.ValidationResponse
import oleh.liskovych.gallerygif.utils.validation.common.ValidationResponseImpl
import oleh.liskovych.gallerygif.utils.validation.common.Validator

// there is some redundant code in the class. It was added intently for demonstration
class PicturePathValidator private constructor( private val emptyError: String,
                                                private val invalidError: String,
                                                private val additionalRegex: Regex?) : Validator {

    override fun validate(picturePath: String?): ValidationResponse {
        val error = if(picturePath.isNullOrEmpty()) emptyError else if (!isPicturePathValid(picturePath!!)) invalidError else ""
        return ValidationResponseImpl(error.isEmpty(), error)
    }

    private fun isPicturePathValid(picturePath: String): Boolean
        = additionalCheck(picturePath)

    private fun additionalCheck(picturePath: String) = additionalRegex?.matches(picturePath) ?: true

    class Builder(aContext: Context) {
        private val context = aContext.applicationContext
        var emptyError: String = context.getString(R.string.default_error_profile_picture_is_absent)
        var invalidError: String = context.getString(R.string.default_error_profile_picture_is_invalid)
        var additionalRegex: Regex? = null

        fun emptyError(@StringRes emptyError: Int) = apply {
            this.emptyError = context.getString(emptyError)
        }

        fun invalidError(@StringRes invalidError: Int) = apply {
            this.invalidError = context.getString(invalidError)
        }

        fun additionalRegex(additionalRegex: Regex?) = apply { this.additionalRegex = additionalRegex }

        fun additionalRegex(additionalRegex: String?) = apply { this.additionalRegex = additionalRegex?.toRegex() }

        fun build() = PicturePathValidator(emptyError, invalidError, additionalRegex)
    }

    companion object {
        fun builder(context: Context) = Builder(context)

        fun getDefaultValidator(context: Context) = builder(context).build()
    }
}