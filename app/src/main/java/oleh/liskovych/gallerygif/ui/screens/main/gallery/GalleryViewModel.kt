package oleh.liskovych.gallerygif.ui.screens.main.gallery

import android.app.Application
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.functions.Consumer
import oleh.liskovych.gallerygif.models.Image
import oleh.liskovych.gallerygif.providers.ProviderInjector.imageProvider
import oleh.liskovych.gallerygif.ui.base.BaseViewModel
import oleh.liskovych.gallerygif.utils.ioToMain

class GalleryViewModel(application: Application) : BaseViewModel(application) {

    val imagesLiveData = MutableLiveData<List<Image>>()
    val emptyListPlaceholderLiveData = MutableLiveData<Boolean>()
    val refreshLiveData = MediatorLiveData<Boolean>()

    init {
        refreshLiveData.apply {
            addSource(imagesLiveData) { this.value = false }
            addSource(errorLiveData) { this.value = false }
        }
    }

    override fun showProgress() {
        refreshLiveData.postValue(true)
    }

    private fun showEmptyListPlaceholder(isEmpty: Boolean) {
        emptyListPlaceholderLiveData.postValue(isEmpty)
    }

    private val imagesConsumer = Consumer<List<Image>> {
        showEmptyListPlaceholder(it.isEmpty())
        imagesLiveData.postValue(it)
    }

    fun loadImages() = imageProvider
        .getAllImages()
        .doOnRequest { showProgress() }
        .compose(ioToMain())
        .subscribe(imagesConsumer, onErrorConsumer)
        .addSubscription()

}