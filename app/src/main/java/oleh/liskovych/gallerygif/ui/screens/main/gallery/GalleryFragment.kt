package oleh.liskovych.gallerygif.ui.screens.main.gallery

import oleh.liskovych.gallerygif.R
import oleh.liskovych.gallerygif.ui.base.BaseFragment

class GalleryFragment: BaseFragment<GalleryViewModel>() {

    override val viewModelClass = GalleryViewModel::class.java

    override val layoutId = R.layout.fragment_gallery

    override fun observeLiveData(viewModel: GalleryViewModel) {
        // do nothing
    }


}