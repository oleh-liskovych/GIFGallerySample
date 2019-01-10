package oleh.liskovych.gallerygif.ui.screens.auth.sign_up

import android.app.Application
import androidx.lifecycle.MutableLiveData
import oleh.liskovych.gallerygif.ui.base.BaseViewModel
import oleh.liskovych.gallerygif.utils.ValidatorFactory
import oleh.liskovych.gallerygif.utils.validation.common.ValidationResponse
import oleh.liskovych.gallerygif.utils.validation.common.Validator

class SignUpViewModel(application: Application): BaseViewModel(application) {

    private val emailValidator: Validator by lazy { ValidatorFactory.emailValidator(application) }

    val isValid = MutableLiveData<Boolean>()
    val isEmailValid = MutableLiveData<ValidationResponse>()
    val isPasswordsValid = MutableLiveData<ValidationResponse>()

    val emailError = MutableLiveData<String>()
    val passwordError = MutableLiveData<String>()

    fun validateUserData(username: String,
                         email: String,
                         password: String) {
        isValid.value = validateEmail(email) and true
    }

    private fun validateEmail(email: String) =
        validate(emailValidator, email, isEmailValid)

    private fun validate(validator: Validator, data: String, validationLiveData: MutableLiveData<ValidationResponse>) =
        let {
            validationLiveData.value = validator.validate(data)
            validationLiveData.value?.isValid
        } ?: false

}