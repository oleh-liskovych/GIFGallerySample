package oleh.liskovych.gallerygif.ui.screens.main.gallery

import android.app.Application
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.functions.Consumer
import oleh.liskovych.gallerygif.models.Gif
import oleh.liskovych.gallerygif.models.Image
import oleh.liskovych.gallerygif.providers.ProviderInjector
import oleh.liskovych.gallerygif.providers.ProviderInjector.imageProvider
import oleh.liskovych.gallerygif.ui.base.BaseViewModel
import oleh.liskovych.gallerygif.utils.ioToMain

class GalleryViewModel(application: Application) : BaseViewModel(application) {

    val imagesLiveData = MutableLiveData<List<Image>>()
    val gifLiveData = MutableLiveData<Gif>()
    val emptyListPlaceholderLiveData = MutableLiveData<Boolean>()
    val refreshLiveData = MediatorLiveData<Boolean>()

    init {
        setLoadingLiveData(gifLiveData)
        refreshLiveData.apply {
            addSource(imagesLiveData) { this.value = false }
            addSource(errorLiveData) { this.value = false }
        }
    }

    private fun showRefresh() {
        refreshLiveData.postValue(true)
    }

    private fun showEmptyListPlaceholder(isEmpty: Boolean) {
        emptyListPlaceholderLiveData.postValue(isEmpty)
    }

    private val imagesConsumer = Consumer<List<Image>> {
        showEmptyListPlaceholder(it.isEmpty())
        imagesLiveData.postValue(it)
    }

    private val gifConsumer = Consumer<Gif> {
        gifLiveData.postValue(it)
    }

    fun loadImages() = imageProvider
        .getAllImages()
        .doOnRequest { showRefresh() }
        .compose(ioToMain())
        .subscribe(imagesConsumer, onErrorConsumer)
        .addSubscription()

    fun loadGif() = imageProvider
        .getGif(null)
        .doOnRequest { showProgress() }
        .compose(ioToMain())
        .subscribe(gifConsumer, onErrorConsumer)
        .addSubscription()
}