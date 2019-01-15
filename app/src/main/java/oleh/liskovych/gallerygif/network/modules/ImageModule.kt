package oleh.liskovych.gallerygif.network.modules

import io.reactivex.Flowable
import okhttp3.RequestBody
import oleh.liskovych.gallerygif.JsonKeywords.DESCRIPTION
import oleh.liskovych.gallerygif.JsonKeywords.HASHTAG
import oleh.liskovych.gallerygif.JsonKeywords.IMAGE
import oleh.liskovych.gallerygif.JsonKeywords.LATITUDE
import oleh.liskovych.gallerygif.JsonKeywords.LONGITUDE
import oleh.liskovych.gallerygif.models.Gif
import oleh.liskovych.gallerygif.models.Image
import oleh.liskovych.gallerygif.network.api.ImageApi
import oleh.liskovych.gallerygif.network.bean.images.ImageBean
import oleh.liskovych.gallerygif.network.converters.image.GifBeanConverter
import oleh.liskovych.gallerygif.network.converters.image.ImageBeanConverter
import oleh.liskovych.gallerygif.utils.NetworkErrorUtils
import oleh.liskovych.gallerygif.utils.NetworkUtils.getMultipartFromFileUri

interface ImageModule {

    fun getAllImages(): Flowable<List<Image>>

    fun getGif(weather: String?): Flowable<Gif>

    fun uploadImage(
        filePath: String,
        description: String?,
        hashtag: String?,
        latitude: Float,
        longitude: Float
    ): Flowable<Image>
}

class ImageModuleImpl(api: ImageApi): BaseRxModule<ImageApi, ImageBean, Image>(api, ImageBeanConverter()) , ImageModule {

    private val gifBeanConverter = GifBeanConverter()

    override fun getAllImages(): Flowable<List<Image>> =
        api.getAllImages()
            .onErrorResumeNext(NetworkErrorUtils.rxParseError())
            .map { it.images }
            .map { converter.convertListInToOut(it) }

    override fun getGif(weather: String?): Flowable<Gif> =
        api.getGif(weather)
            .onErrorResumeNext(NetworkErrorUtils.rxParseError())
            .map { gifBeanConverter.convertInToOut(it) }

    override fun uploadImage(
        filePath: String,
        description: String?,
        hashtag: String?,
        latitude: Float,
        longitude: Float
    ): Flowable<Image> =
        api.uploadImage(
            getMultipartFromFileUri(filePath, IMAGE),
            formUploadRequest(description, hashtag, latitude, longitude))
            .onErrorResumeNext(NetworkErrorUtils.rxParseError())
            .map { converter.convertInToOut(it) }

    private fun formUploadRequest(
        description: String?,
        hashtag: String?,
        latitude: Float,
        longitude: Float
    ): Map<String, RequestBody> = LinkedHashMap<String, RequestBody>().apply {

        description?.takeIf { it.isNotBlank() }?.let {
            put(DESCRIPTION, RequestBody.create(null, it))
        }
        hashtag?.takeIf { it.isNotBlank() }?.let {
            put(HASHTAG, RequestBody.create(null, it))
        }
        put(LATITUDE, RequestBody.create(null, latitude.toString()))
        put(LONGITUDE, RequestBody.create(null, longitude.toString()))
    }

}