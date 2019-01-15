package oleh.liskovych.gallerygif.network.api

import io.reactivex.Flowable
import okhttp3.MultipartBody
import okhttp3.RequestBody
import oleh.liskovych.gallerygif.Endpoints.CREATE
import oleh.liskovych.gallerygif.Endpoints.LOGIN
import oleh.liskovych.gallerygif.network.bean.UserBean
import oleh.liskovych.gallerygif.network.request.SignInRequest
import retrofit2.http.*

interface UserApi {

    @Multipart
    @POST(CREATE)
    fun signUp(@PartMap parts: Map<String, @JvmSuppressWildcards RequestBody>, @Part avatar: MultipartBody.Part): Flowable<UserBean>

    @POST(LOGIN)
    fun signIn(@Body request: SignInRequest): Flowable<UserBean>
}