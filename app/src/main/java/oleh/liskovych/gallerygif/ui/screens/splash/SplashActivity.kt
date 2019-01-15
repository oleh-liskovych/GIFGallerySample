package oleh.liskovych.gallerygif.ui.screens.splash

import android.app.Activity
import android.os.Bundle
import oleh.liskovych.gallerygif.PreferencesProvider
import oleh.liskovych.gallerygif.ui.screens.auth.AuthActivity
import oleh.liskovych.gallerygif.ui.screens.main.MainActivity

class SplashActivity: Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (PreferencesProvider.token.isEmpty())
            AuthActivity.start(this)
        else
            MainActivity.start(this)

        finish()
    }
}