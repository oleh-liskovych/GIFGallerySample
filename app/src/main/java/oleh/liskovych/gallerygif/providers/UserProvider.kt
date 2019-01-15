package oleh.liskovych.gallerygif.providers

import io.reactivex.Flowable
import oleh.liskovych.gallerygif.PreferencesProvider
import oleh.liskovych.gallerygif.models.User
import oleh.liskovych.gallerygif.models.UserModel
import oleh.liskovych.gallerygif.network.SNetworkModule
import oleh.liskovych.gallerygif.network.modules.UserModule
import oleh.liskovych.gallerygif.providers.base.BaseOnlineProvider
import oleh.liskovych.gallerygif.providers.base.Provider


class UserProvider: BaseOnlineProvider<User, UserModule>(), Provider<User> {

    override fun initNetworkModule(): UserModule = SNetworkModule.getUserModule()

    fun signUp(filePath: String,
               username: String,
               email: String,
               password:String): Flowable<User> =
            networkModule
                .signUp(filePath, username, email, password)
                .map { user ->
                    user.apply { token?.let { PreferencesProvider.token = it } }
                }

    fun signIn(email: String,
               password:String): Flowable<User> =
            networkModule
                .signIn(email, password)
                .map { user ->
                    user.apply { token?.let { PreferencesProvider.token = it } }
                }

}
