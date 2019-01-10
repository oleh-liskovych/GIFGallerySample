package oleh.liskovych.gallerygif.ui.screens.main

import oleh.liskovych.gallerygif.R
import oleh.liskovych.gallerygif.ui.base.BaseActivity

class MainActivity : BaseActivity<MainViewModel>() {

    override val viewModelClass = MainViewModel::class.java
    override val layoutId = R.layout.activity_main

    override fun observeLiveData(viewModel: MainViewModel) {
        // do nothing
    }

}
