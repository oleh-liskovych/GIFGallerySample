package oleh.liskovych.gallerygif.ui.screens.auth.sign_in

import android.app.Application
import androidx.lifecycle.MutableLiveData
import io.reactivex.functions.Consumer
import oleh.liskovych.gallerygif.models.User
import oleh.liskovych.gallerygif.network.SNetworkModule
import oleh.liskovych.gallerygif.network.modules.UserModule
import oleh.liskovych.gallerygif.providers.ProviderInjector
import oleh.liskovych.gallerygif.providers.UserProvider
import oleh.liskovych.gallerygif.ui.base.BaseViewModel
import oleh.liskovych.gallerygif.utils.ValidatorFactory
import oleh.liskovych.gallerygif.utils.ioToMain
import oleh.liskovych.gallerygif.utils.validation.common.ValidationResponse
import oleh.liskovych.gallerygif.utils.validation.common.Validator

class SignInViewModel(application: Application): BaseViewModel(application) {

    private val userProvider: UserProvider by lazy { ProviderInjector.getUserProvider() }

    private val emailValidator: Validator by lazy { ValidatorFactory.emailValidator(application) }
    private val passwordValidator: Validator by lazy { ValidatorFactory.passwordValidator(application) }

    val isValid = MutableLiveData<Boolean>()
    val isEmailValid = MutableLiveData<ValidationResponse>()
    val isPasswordsValid = MutableLiveData<ValidationResponse>()
    val isSignInSuccess = MutableLiveData<Boolean>()

    val emailError = MutableLiveData<String>()
    val passwordError = MutableLiveData<String>()

    private val signInSuccessConsumer = Consumer<User> {
        isSignInSuccess.postValue(true)
    }

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
        showProgress()
        userProvider.signIn(email, password)
            .doOnEach { hideProgress() }
            .compose(ioToMain())
            .subscribe(signInSuccessConsumer, onErrorConsumer)
            .addSubscription()
    }

}