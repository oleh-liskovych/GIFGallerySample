package oleh.liskovych.gallerygif.ui.screens.auth.sign_in

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.fragment_sign_in.*
import kotlinx.android.synthetic.main.include_auth_pack.*
import oleh.liskovych.gallerygif.R
import oleh.liskovych.gallerygif.R.id.to_main
import oleh.liskovych.gallerygif.R.id.to_sign_up
import oleh.liskovych.gallerygif.extensions.markFieldsRequired
import oleh.liskovych.gallerygif.extensions.setClickListeners
import oleh.liskovych.gallerygif.ui.base.BaseFragment
import oleh.liskovych.gallerygif.utils.bindInterfaceOrThrow

class SignInFragment: BaseFragment<SignInViewModel>(), View.OnClickListener {

    override val viewModelClass = SignInViewModel::class.java
    override val layoutId = R.layout.fragment_sign_in

    private var callback: SignInCallback? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setClickListeners(bSend, bSignUp)
        markFieldsRequired(tilEmail, tilPassword)
    }

    override fun observeLiveData(viewModel: SignInViewModel) {
        // do nothing
    }

    override fun onClick(view: View) {
        when(view.id) {
            R.id.bSend -> navigateToMain()
            R.id.bSignUp -> navigateToSignUp()
        }
    }

    private fun navigateToSignUp() {
        findNavController().navigate(to_sign_up, null, null)
    }

    private fun navigateToMain() {
        findNavController().navigate(to_main, null, null)
        callback?.finishActivity()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callback = bindInterfaceOrThrow<SignInCallback>(context, parentFragment)
    }

    override fun onDetach() {
        callback = null
        super.onDetach()
    }

}