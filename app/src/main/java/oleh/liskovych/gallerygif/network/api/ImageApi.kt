package oleh.liskovych.gallerygif.network.api

import io.reactivex.Flowable
import okhttp3.MultipartBody
import okhttp3.RequestBody
import oleh.liskovych.gallerygif.Endpoints.ALL
import oleh.liskovych.gallerygif.Endpoints.GIF
import oleh.liskovych.gallerygif.Endpoints.IMAGE
import oleh.liskovych.gallerygif.JsonKeywords.WEATHER
import oleh.liskovych.gallerygif.network.bean.GifBean
import oleh.liskovych.gallerygif.network.bean.images.ImageBean
import oleh.liskovych.gallerygif.network.bean.images.ImagesContainerBean
import retrofit2.http.*

interface ImageApi {

    @GET(ALL)
    fun getAllImages(): Flowable<ImagesContainerBean>

    @GET(GIF)
    fun getGif(@Path(WEATHER) weather: String?): Flowable<GifBean>

    @Multipart
    @POST(IMAGE)
    fun uploadImage(@Part image: MultipartBody.Part,
                    @PartMap parts: Map<String, @JvmSuppressWildcards RequestBody>): Flowable<ImageBean>

}