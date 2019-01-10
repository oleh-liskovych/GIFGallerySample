package oleh.liskovych.gallerygif.ui.screens.auth.sign_up

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.TextInputEditText
import kotlinx.android.synthetic.main.include_auth_pack.*
import oleh.liskovych.gallerygif.R
import oleh.liskovych.gallerygif.extensions.getStringText
import oleh.liskovych.gallerygif.extensions.markFieldsRequired
import oleh.liskovych.gallerygif.extensions.setClickListeners
import oleh.liskovych.gallerygif.ui.base.BaseFragment
import oleh.liskovych.gallerygif.utils.bindInterfaceOrThrow
import oleh.liskovych.gallerygif.utils.validation.common.ValidationResponse

class SignUpFragment: BaseFragment<SignUpViewModel>(), View.OnClickListener {

    override val viewModelClass = SignUpViewModel::class.java
    override val layoutId = R.layout.fragment_sign_up

    private var callback: SignUpCallback? = null

    override fun observeLiveData(viewModel: SignUpViewModel) {
        with(viewModel) {
            isValid.observe(this@SignUpFragment, validationObserver)
            isEmailValid.observe(this@SignUpFragment, emailObserver)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setClickListeners(bSend)
        markFieldsRequired(tilEmail, tilPassword)
    }

    override fun onClick(view: View) {
        when(view.id) {
            R.id.bSend -> validateUserData()
        }
    }

    private val validationObserver = Observer<Boolean> {
        if (it) navigateToMain()
    }

    private val emailObserver = Observer<ValidationResponse> {
        showValidationError(etEmail, it)
    }

    private fun showValidationError(textInput: TextInputEditText, response: ValidationResponse) {
        response.run { if (!isValid) showErrorInField(textInput, errorMessage) }
    }

    private fun showErrorInField(textInput: TextInputEditText, error: String?) {
        textInput.error = error
    }

    private fun validateUserData() = viewModel.validateUserData(
        "", etEmail.getStringText(), ""
    )

    private fun navigateToMain() {
        findNavController().navigate(R.id.to_main, null, null)
        callback?.finishActivity()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callback = bindInterfaceOrThrow<SignUpCallback>(context, parentFragment)
    }

    override fun onDetach() {
        callback = null
        super.onDetach()
    }

}