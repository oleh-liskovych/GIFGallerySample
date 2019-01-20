package oleh.liskovych.gallerygif.ui.base

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import oleh.liskovych.gallerygif.utils.withNotNull

abstract class BaseViewModel(application: Application): AndroidViewModel(application) {

    companion object {
        private const val EMPTY_ERROR = ""
    }

    private val backgroundSubscriptions = CompositeDisposable()

    val errorLiveData = MediatorLiveData<String>()

    val isLoadingLiveData = MediatorLiveData<Boolean>()

    val onErrorConsumer = Consumer<Throwable> {
//        val errorString = parseApiException(it)   // todo: Parse Api Exception
        errorLiveData.postValue(it.message)
        hideProgress()
    }

    fun setLoadingLiveData(vararg liveDataArgs: MutableLiveData<*>) {
        liveDataArgs.forEach { liveData ->
            isLoadingLiveData.apply {
                this.removeSource(liveData)
                this.addSource(liveData) { this.postValue(false) }
            }
        }
    }

    private fun clearSubscriptions() {
        backgroundSubscriptions.run { if (!isDisposed) dispose() }
    }

    override fun onCleared() {
        isLoadingLiveData.postValue(false)
        clearSubscriptions()
        super.onCleared()
    }

    open fun showProgress() {
        isLoadingLiveData.postValue(true)
    }

    fun hideProgress() {
        isLoadingLiveData.postValue(false)
    }

    private fun showProgress(show: Boolean) {
        isLoadingLiveData.postValue(show)
    }

//    fun parseApiException(throwable: Throwable) =
//            withNotNull(throwable as? Api)

    fun Disposable.addSubscription() = backgroundSubscriptions.add(this)

}