package oleh.liskovych.gallerygif.ui.screens.auth

import android.content.Context
import android.os.Bundle
import android.view.WindowManager
import oleh.liskovych.gallerygif.R
import oleh.liskovych.gallerygif.ui.base.BaseActivity
import oleh.liskovych.gallerygif.ui.base.interfaces.BaseView
import oleh.liskovych.gallerygif.ui.screens.auth.sign_in.SignInCallback
import oleh.liskovych.gallerygif.ui.screens.auth.sign_up.SignUpCallback
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.newTask

class AuthActivity : BaseActivity<AuthViewModel>(),
    SignInCallback,
    SignUpCallback {

    companion object {
        fun start(context: Context) {
            with(context) {
                intentFor<AuthActivity>()
                    .newTask()
                    .let { startActivity(it) }
            }
        }
    }

    override val viewModelClass = AuthViewModel::class.java
    override val layoutId = R.layout.activity_auth

    override fun hasProgressBar() = true

    override fun observeLiveData(viewModel: AuthViewModel) {
        // do nothing
    }

    override fun finishActivity() {
        finish()
    }

}