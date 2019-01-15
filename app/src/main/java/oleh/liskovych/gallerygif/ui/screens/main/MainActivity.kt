package oleh.liskovych.gallerygif.ui.screens.main

import android.content.Context
import oleh.liskovych.gallerygif.R
import oleh.liskovych.gallerygif.ui.base.BaseActivity
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.newTask

class MainActivity : BaseActivity<MainViewModel>() {

    override val viewModelClass = MainViewModel::class.java
    override val layoutId = R.layout.activity_main

    companion object {
        fun start(context: Context) {
            with(context) {
                intentFor<MainActivity>()
                    .newTask()
                    .let { startActivity(it) }
            }
        }
    }

    override fun observeLiveData(viewModel: MainViewModel) {
        // do nothing
    }

}
