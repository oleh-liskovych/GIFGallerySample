package oleh.liskovych.gallerygif.ui.screens.auth.sign_up

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import io.reactivex.functions.Consumer
import oleh.liskovych.gallerygif.models.User
import oleh.liskovych.gallerygif.providers.ProviderInjector
import oleh.liskovych.gallerygif.providers.ProviderInjector.userProvider
import oleh.liskovych.gallerygif.providers.UserProvider
import oleh.liskovych.gallerygif.ui.base.BaseViewModel
import oleh.liskovych.gallerygif.utils.ValidatorFactory
import oleh.liskovych.gallerygif.utils.ioToMain
import oleh.liskovych.gallerygif.utils.validation.common.ValidationResponse
import oleh.liskovych.gallerygif.utils.validation.common.Validator

class SignUpViewModel(application: Application): BaseViewModel(application) {

    private val emailValidator: Validator by lazy { ValidatorFactory.emailValidator(application) }
    private val passwordValidator: Validator by lazy { ValidatorFactory.passwordValidator(application) }
    private val picturePathValidator: Validator by lazy { ValidatorFactory.picturePathValidator(application) }

    val isValid = MutableLiveData<Boolean>()
    val isEmailValid = MutableLiveData<ValidationResponse>()
    val isPasswordsValid = MutableLiveData<ValidationResponse>()
    val isPicturePathValid = MutableLiveData<ValidationResponse>()
    val isSignUpSuccess = MutableLiveData<Boolean>()

    val emailError = MutableLiveData<String>()
    val passwordError = MutableLiveData<String>()

    init {
        isLoadingLiveData.apply {
            addSource(isSignUpSuccess) { this.value = false }
            addSource(errorLiveData) { this.value = false }
        }
    }

    private val signUpSuccessConsumer = Consumer<User> {
        isSignUpSuccess.postValue(true)
    }

    private val onSignUpError = Consumer<Throwable> {
        // todo: Implement this method
    }

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

    fun sendSignUpRequest(filePath: String,
                          username: String,
                          email: String,
                          password:String) {
        userProvider
            .signUp(filePath, username, email, password)
            .doOnRequest { showProgress() }
            .doOnEach { hideProgress() }
            .compose(ioToMain())
            .subscribe(signUpSuccessConsumer, onSignUpError)
            .addSubscription()
    }
}