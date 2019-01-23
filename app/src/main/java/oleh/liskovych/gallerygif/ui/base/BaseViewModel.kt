package oleh.liskovych.gallerygif.ui.base

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import oleh.liskovych.gallerygif.network.exceptions.ApiException

abstract class BaseViewModel(application: Application): AndroidViewModel(application) {

    companion object {
        private const val EMPTY_ERROR = ""
    }

    private val backgroundSubscriptions = CompositeDisposable()

    val errorLiveData = MediatorLiveData<String>()

    val isLoadingLiveData = MediatorLiveData<Boolean>()

    val onErrorConsumer = Consumer<Throwable> {
        val errorString = parseApiException(it)
        errorLiveData.postValue(errorString)
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

    fun Disposable.addSubscription() = backgroundSubscriptions.add(this)

    private fun parseApiException(throwable: Throwable): String =
        (throwable as? ApiException)?.mMessage ?: EMPTY_ERROR

}