package oleh.liskovych.gallerygif.ui.screens.splash

import android.app.Activity
import android.os.Bundle
import oleh.liskovych.gallerygif.ui.screens.auth.AuthActivity

class SplashActivity: Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // todo: check token in Preferences. Depending on this navigate to AuthActivity or to MainActivity

        AuthActivity.start(this)
        finish()
    }
}