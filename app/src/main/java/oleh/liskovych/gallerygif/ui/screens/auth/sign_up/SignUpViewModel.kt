package oleh.liskovych.gallerygif.ui.screens.auth.sign_up

import android.app.Application
import androidx.lifecycle.MutableLiveData
import oleh.liskovych.gallerygif.network.SNetworkModule
import oleh.liskovych.gallerygif.network.modules.UserModule
import oleh.liskovych.gallerygif.network.modules.UserModuleImpl
import oleh.liskovych.gallerygif.ui.base.BaseViewModel
import oleh.liskovych.gallerygif.utils.ValidatorFactory
import oleh.liskovych.gallerygif.utils.ioToMain
import oleh.liskovych.gallerygif.utils.validation.common.ValidationResponse
import oleh.liskovych.gallerygif.utils.validation.common.Validator
import java.util.function.Consumer

class SignUpViewModel(application: Application): BaseViewModel(application) {

    private val userModule: UserModule by lazy { SNetworkModule.getUserModule() }

    private val emailValidator: Validator by lazy { ValidatorFactory.emailValidator(application) }
    private val passwordValidator: Validator by lazy { ValidatorFactory.passwordValidator(application) }
    private val picturePathValidator: Validator by lazy { ValidatorFactory.picturePathValidator(application) }

    val isValid = MutableLiveData<Boolean>()
    val isEmailValid = MutableLiveData<ValidationResponse>()
    val isPasswordsValid = MutableLiveData<ValidationResponse>()
    val isPicturePathValid = MutableLiveData<ValidationResponse>()

    val emailError = MutableLiveData<String>()
    val passwordError = MutableLiveData<String>()

    fun validateUserData(picturePath: String?,
                         email: String,
                         password: String) {
        isValid.value = validatePicturePath(picturePath) and
                        validateEmail(email) and
                        validatePassword(password)
    }

    private fun validatePicturePath(picturePath: String?) =
        validate(picturePathValidator, picturePath, isPicturePathValid)

    private fun validateEmail(email: String) =
        validate(emailValidator, email, isEmailValid)

    private fun validatePassword(password: String) =
        validate(passwordValidator, password, isPasswordsValid)


    private fun validate(validator: Validator, data: String?, validationLiveData: MutableLiveData<ValidationResponse>) =
        let {
            validationLiveData.value = validator.validate(data)
            validationLiveData.value?.isValid
        } ?: false

    private val onSignUpError = Consumer<Throwable> {

    }

    fun sendSignUpRequest(filePath: String,
                          username: String,
                          email: String,
                          password:String) {
        isLoadingLiveData.value = true
        userModule.signUp(filePath, username, email, password)
            .compose(ioToMain())
            .subscribe()
    }
}