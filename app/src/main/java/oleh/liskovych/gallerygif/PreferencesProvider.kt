package oleh.liskovych.gallerygif

internal object PreferencesProvider {

    private const val TOKEN: String = "TOKEN"

    private val preferences = App.securePrefs

    var token: String
        get() = preferences.getString(TOKEN, EMPTY_STRING) ?: EMPTY_STRING
        set(value) {
            preferences.edit()
                .putString(TOKEN, value)
                .commit()
        }

}