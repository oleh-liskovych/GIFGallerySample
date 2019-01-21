package oleh.liskovych.gallerygif.ui.screens.main.upload

import android.app.Application
import androidx.lifecycle.MutableLiveData
import io.reactivex.functions.Consumer
import oleh.liskovych.gallerygif.models.Image
import oleh.liskovych.gallerygif.providers.ProviderInjector.imageProvider
import oleh.liskovych.gallerygif.ui.base.BaseViewModel
import oleh.liskovych.gallerygif.utils.ioToMain

class UploadViewModel(application: Application): BaseViewModel(application) {

    val uploadedImageLiveData = MutableLiveData<Image>()

    init {
        setLoadingLiveData(uploadedImageLiveData)
    }

    private val imageConsumer = Consumer<Image> {
        uploadedImageLiveData.postValue(it)
    }

    fun uploadImage(picturePath: String,
                    description: String,
                    hashtag: String,
                    latitude: Float,
                    longitude: Float) {
        imageProvider
            .uploadImage(picturePath, description, hashtag, latitude, longitude)
            .doOnRequest { showProgress() }
            .compose(ioToMain())
            .subscribe(imageConsumer, onErrorConsumer)
            .addSubscription()
    }

}