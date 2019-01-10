package oleh.liskovych.gallerygif.utils

import android.content.Context
import oleh.liskovych.gallerygif.utils.validation.EmailValidator
import java.util.regex.Pattern

object ValidatorFactory {

    private val PASSWORD_PATTERN = Pattern.compile("^(?=.*[0-9])(?=.*[a-zA-Z]).+\$")

    fun emailValidator(context: Context) = EmailValidator.builder(context).build()

}