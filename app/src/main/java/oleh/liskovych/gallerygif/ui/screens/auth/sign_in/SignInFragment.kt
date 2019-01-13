package oleh.liskovych.gallerygif.ui.screens.auth.sign_in

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.TextInputEditText
import kotlinx.android.synthetic.main.fragment_sign_in.*
import kotlinx.android.synthetic.main.include_auth_pack.*
import oleh.liskovych.gallerygif.R
import oleh.liskovych.gallerygif.R.id.to_main
import oleh.liskovych.gallerygif.R.id.to_sign_up
import oleh.liskovych.gallerygif.extensions.getStringText
import oleh.liskovych.gallerygif.extensions.hideInputFieldsErrors
import oleh.liskovych.gallerygif.extensions.markFieldsRequired
import oleh.liskovych.gallerygif.extensions.setClickListeners
import oleh.liskovych.gallerygif.ui.base.BaseFragment
import oleh.liskovych.gallerygif.utils.bindInterfaceOrThrow
import oleh.liskovych.gallerygif.utils.validation.common.ValidationResponse

class SignInFragment: BaseFragment<SignInViewModel>(), View.OnClickListener {

    override val viewModelClass = SignInViewModel::class.java
    override val layoutId = R.layout.fragment_sign_in

    private var callback: SignInCallback? = null

    private val validationObserver = Observer<Boolean> {
        signIn(it)
    }

    private val emailObserver = Observer<ValidationResponse> {
        showValidationError(etEmail, it)
    }

    private val passwordObserver = Observer<ValidationResponse> {
        showValidationError(etPassword, it)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setClickListeners(bSend, bSignUp)
        markFieldsRequired(tilEmail, tilPassword)
    }

    override fun observeLiveData(viewModel: SignInViewModel) {
        with(viewModel) {
            isValid.observe(this@SignInFragment, validationObserver)
            isEmailValid.observe(this@SignInFragment, emailObserver)
            isPasswordsValid.observe(this@SignInFragment, passwordObserver)
        }
    }

    override fun onClick(view: View) {
        when(view.id) {
            R.id.bSend -> validateFields()
            R.id.bSignUp -> navigateToSignUp()
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callback = bindInterfaceOrThrow<SignInCallback>(context, parentFragment)
    }

    override fun onDetach() {
        callback = null
        super.onDetach()
    }

    private fun signIn(valid: Boolean) {
        if (valid) { viewModel.sendSignInRequest(etEmail.getStringText(), etPassword.getStringText()) }
    }

    private fun showValidationError(textInput: TextInputEditText, response: ValidationResponse) {
        response.run { if (!isValid) showErrorInField(textInput, errorMessage) }
    }

    private fun showErrorInField(textInput: TextInputEditText, error: String?) {
        textInput.error = error
    }

    private fun validateFields() {
        hideInputFieldsErrors(etEmail, etPassword)
        validateUserData()
    }

    private fun validateUserData() = viewModel.validateUserData(
        etEmail.getStringText(), etPassword.getStringText()
    )

    private fun navigateToSignUp() {
        findNavController().navigate(to_sign_up, null, null)
    }

    private fun navigateToMain() {
        findNavController().navigate(to_main, null, null)
        callback?.finishActivity()
    }

}