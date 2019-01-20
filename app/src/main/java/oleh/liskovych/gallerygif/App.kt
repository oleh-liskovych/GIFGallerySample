package oleh.liskovych.gallerygif

import android.app.Application
import com.securepreferences.SecurePreferences

class App: Application() {

    companion object {
        private val TAG = App::class.java.simpleName

        lateinit var instance: App
            private set
        lateinit var securePrefs: SecurePreferences
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        securePrefs = SecurePreferences(this,
            BuildConfig.SECURE_PREF_PASSWORD,
            BuildConfig.SECURE_PREF_NAME)
    }

    fun logout(){
        PreferencesProvider.token = EMPTY_STRING
    }
}