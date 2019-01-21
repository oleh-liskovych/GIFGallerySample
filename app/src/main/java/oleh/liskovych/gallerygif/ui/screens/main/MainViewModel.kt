package oleh.liskovych.gallerygif.ui.screens.main

import android.app.Application
import androidx.lifecycle.MutableLiveData
import oleh.liskovych.gallerygif.ui.base.BaseViewModel

class MainViewModel(application: Application): BaseViewModel(application) {

    val itemAddedLiveData = MutableLiveData<Boolean>()

}