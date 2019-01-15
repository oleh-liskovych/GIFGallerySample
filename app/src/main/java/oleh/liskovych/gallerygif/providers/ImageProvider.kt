package oleh.liskovych.gallerygif.providers

import io.reactivex.Flowable
import oleh.liskovych.gallerygif.models.Gif
import oleh.liskovych.gallerygif.models.Image
import oleh.liskovych.gallerygif.models.Model
import oleh.liskovych.gallerygif.network.SNetworkModule
import oleh.liskovych.gallerygif.network.modules.ImageModule
import oleh.liskovych.gallerygif.providers.base.BaseOnlineProvider
import oleh.liskovych.gallerygif.providers.base.Provider

class ImageProvider: BaseOnlineProvider<Model, ImageModule>(), Provider<Model> {

    override fun initNetworkModule(): ImageModule  = SNetworkModule.getImageModule()

    fun getAllImages(): Flowable<List<Image>> =
        networkModule.getAllImages()


    fun getGif(weather: String?): Flowable<Gif> =
        networkModule.getGif(weather)

    fun uploadImage(filePath: String, description: String?,
                    hashtag: String?, latitude: Float, longitude: Float): Flowable<Image> =
        networkModule.uploadImage(filePath, description, hashtag, latitude, longitude)
}