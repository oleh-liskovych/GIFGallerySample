package oleh.liskovych.gallerygif.network.modules

import io.reactivex.Flowable
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import oleh.liskovych.gallerygif.models.User
import oleh.liskovych.gallerygif.network.api.UserApi
import oleh.liskovych.gallerygif.network.bean.UserBean
import oleh.liskovych.gallerygif.network.converters.UserBeanConverter
import oleh.liskovych.gallerygif.network.request.SignInRequest
import java.io.File

interface UserModule {

    fun signUp(filePath: String,
               username: String,
               email: String,
               password:String): Flowable<User>

    fun signIn(email: String,
               password: String): Flowable<User>
}

class UserModuleImpl(api: UserApi): BaseRxModule<UserApi, UserBean, User>(api, UserBeanConverter()), UserModule {

    companion object {
        private const val FORM_DATA_NAME = "avatar"
        private const val FORM_DATA_TYPE = "multipart/form-data"
    }

    private val converterUserBean = UserBeanConverter()

    override fun signUp(filePath: String, username: String, email: String, password: String): Flowable<User> =
        api.signUp(formSignUp(username, email, password), getMultipartFromFileUri(filePath))
            .map { converterUserBean.convertInToOut(it) }

    override fun signIn(email: String, password: String): Flowable<User> =
        api.signIn(SignInRequest(email, "")) // todo: Fix here
            .map { converterUserBean.convertInToOut(it) }


    private fun formSignUp(username: String, email: String, password: String) : Map<String, RequestBody>
            = with(LinkedHashMap<String, RequestBody>()){
            if (username.isNotBlank()) {
                put("username", RequestBody.create(null, username))
            }
            put("email", RequestBody.create(null, email))
            put("password", RequestBody.create(null, password))
            return@with this
        }

    private fun getMultipartFromFileUri(filePath: String): MultipartBody.Part {
        val file = File(filePath)
        return getMultipartFromFileUri(file)
    }

    private fun getMultipartFromFileUri(file: File): MultipartBody.Part {
        val filePart = RequestBody.create(MediaType.parse(FORM_DATA_TYPE), file)
        return MultipartBody.Part.createFormData(FORM_DATA_NAME, file.name, filePart)
    }
}