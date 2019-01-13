package oleh.liskovych.gallerygif.network.modules

import io.reactivex.Flowable

interface ImageModule {

    fun getAllImages(): Flowable<List<Any>>

    fun getGif(): Flowable<Any>

    fun uploadImage(filePath: String,
                    description: String?,
                    hashtag: String?,
                    latitude: Float,
                    longitude: Float): Flowable<Any>
}