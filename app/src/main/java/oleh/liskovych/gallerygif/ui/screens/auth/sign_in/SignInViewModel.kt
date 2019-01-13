package oleh.liskovych.gallerygif.ui.screens.auth.sign_in

import android.app.Application
import androidx.lifecycle.MutableLiveData
import oleh.liskovych.gallerygif.network.SNetworkModule
import oleh.liskovych.gallerygif.network.modules.UserModule
import oleh.liskovych.gallerygif.ui.base.BaseViewModel
import oleh.liskovych.gallerygif.utils.ValidatorFactory
import oleh.liskovych.gallerygif.utils.ioToMain
import oleh.liskovych.gallerygif.utils.validation.common.ValidationResponse
import oleh.liskovych.gallerygif.utils.validation.common.Validator

class SignInViewModel(application: Application): BaseViewModel(application) {

    private val userModule: UserModule by lazy { SNetworkModule.getUserModule() }

    private val emailValidator: Validator by lazy { ValidatorFactory.emailValidator(application) }
    private val passwordValidator: Validator by lazy { ValidatorFactory.passwordValidator(application) }

    val isValid = MutableLiveData<Boolean>()
    val isEmailValid = MutableLiveData<ValidationResponse>()
    val isPasswordsValid = MutableLiveData<ValidationResponse>()

    val emailError = MutableLiveData<String>()
    val passwordError = MutableLiveData<String>()

    fun validateUserData(email: String,
                         password: String) {
        isValid.value = validateEmail(email) and
                        validatePassword(password)
    }

    private fun validateEmail(email: String) =
        validate(emailValidator, email, isEmailValid)

    private fun validatePassword(password: String) =
        validate(passwordValidator, password, isPasswordsValid)


    private fun validate(validator: Validator, data: String, validationLiveData: MutableLiveData<ValidationResponse>) =
        let {
            validationLiveData.value = validator.validate(data)
            validationLiveData.value?.isValid
        } ?: false

    fun sendSignInRequest(email: String,
                          password:String) {
        isLoadingLiveData.value = true
        userModule.signIn(email, password)
            .compose(ioToMain())
            .subscribe()
    }

}