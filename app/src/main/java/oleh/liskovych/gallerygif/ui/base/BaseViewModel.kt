package oleh.liskovych.gallerygif.ui.base

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import java.util.function.Consumer

abstract class BaseViewModel(application: Application): AndroidViewModel(application) {

    companion object {
        private const val EMPTY_ERROR = ""
    }

    private val backgroundSubscriptions = CompositeDisposable()

    val errorLiveData = MediatorLiveData<String>()

    val isLoadingLiveData = MediatorLiveData<Boolean>()

    val onErrorConsumer = Consumer<Throwable> {
        errorLiveData.value = it.message
    }

    fun setLoadingLiveData(vararg liveDataArgs: MutableLiveData<*>) {
        liveDataArgs.forEach { liveData ->
            isLoadingLiveData.apply {
                this.removeSource(liveData)
                this.addSource(liveData) { this.value = false }
            }
        }
    }

    private fun clearSubscriptions() {
        backgroundSubscriptions.run { if (!isDisposed) dispose() }
    }

    override fun onCleared() {
        isLoadingLiveData.value = false
        clearSubscriptions()
        super.onCleared()
    }

    fun showProgress() {
        isLoadingLiveData.value = true
    }

    fun hideProgress() {
        isLoadingLiveData.value = false
    }

    private fun showProgress(show: Boolean) {
        isLoadingLiveData.value = show
    }

    fun Disposable.addSubscription() = backgroundSubscriptions.add(this)

}