package oleh.liskovych.gallerygif.network.modules

import io.reactivex.Flowable
import okhttp3.RequestBody
import oleh.liskovych.gallerygif.JsonKeywords.AVATAR
import oleh.liskovych.gallerygif.JsonKeywords.EMAIL
import oleh.liskovych.gallerygif.JsonKeywords.PASSWORD
import oleh.liskovych.gallerygif.JsonKeywords.USERNAME
import oleh.liskovych.gallerygif.models.User
import oleh.liskovych.gallerygif.network.api.UserApi
import oleh.liskovych.gallerygif.network.bean.UserBean
import oleh.liskovych.gallerygif.network.converters.UserBeanConverter
import oleh.liskovych.gallerygif.network.request.SignInRequest
import oleh.liskovych.gallerygif.utils.NetworkErrorUtils
import oleh.liskovych.gallerygif.utils.NetworkUtils.getMultipartFromFileUri

interface UserModule {

    fun signUp(filePath: String,
               username: String,
               email: String,
               password:String): Flowable<User>

    fun signIn(email: String,
               password: String): Flowable<User>
}

class UserModuleImpl(api: UserApi): BaseRxModule<UserApi, UserBean, User>(api, UserBeanConverter()), UserModule {

    override fun signUp(filePath: String, username: String, email: String, password: String): Flowable<User> =
        api.signUp(formSignUp(username, email, password), getMultipartFromFileUri(filePath, AVATAR))
            .onErrorResumeNext(NetworkErrorUtils.rxParseError())
            .map { converter.convertInToOut(it) }

    override fun signIn(email: String, password: String): Flowable<User> =
        api.signIn(SignInRequest(email, password))
            .onErrorResumeNext(NetworkErrorUtils.rxParseError())
            .map { converter.convertInToOut(it) }


    private fun formSignUp(username: String, email: String, password: String) : Map<String, RequestBody>
            = with(LinkedHashMap<String, RequestBody>()){
            if (username.isNotBlank()) {
                put(USERNAME, RequestBody.create(null, username))
            }
            put(EMAIL, RequestBody.create(null, email))
            put(PASSWORD, RequestBody.create(null, password))
            return@with this
        }
}